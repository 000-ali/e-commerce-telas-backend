package com.ecommerce.telas_backend.service;

import com.ecommerce.telas_backend.dto.ProdutoRequestDTO;
import com.ecommerce.telas_backend.dto.ProdutoResponseDTO;
import com.ecommerce.telas_backend.model.Produto;
import com.ecommerce.telas_backend.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final NotificacaoService notificacaoService;

    public ProdutoService(ProdutoRepository produtoRepository, NotificacaoService notificacaoService) {
        this.produtoRepository = produtoRepository;
        this.notificacaoService = notificacaoService;
    }

   
    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO request) {
        validarRequest(request);

        Produto produto = new Produto(
                request.getNome(),
                request.getArtista(),
                request.getPreco()
        );

        if (request.getDisponivel() != null) {
            produto.setDisponivel(request.getDisponivel());
        }

        Produto salvo = produtoRepository.save(produto);
        System.out.println("[ProdutoService] Produto cadastrado: " + salvo.getNome());
        return ProdutoResponseDTO.fromEntity(salvo);
    }


    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> listarDisponiveis() {
        return produtoRepository.findByDisponivelTrue()
                .stream()
                .map(ProdutoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

   
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = buscarEntidadePorId(id);
        return ProdutoResponseDTO.fromEntity(produto);
    }

    
    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(ProdutoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

   
    public List<ProdutoResponseDTO> filtrarPorArtista(String artista) {
        return produtoRepository.findByArtistaContainingIgnoreCaseAndDisponivelTrue(artista)
                .stream()
                .map(ProdutoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    
    @Transactional
    public ProdutoResponseDTO editar(Long id, ProdutoRequestDTO request) {
        validarRequest(request);

        Produto produto = buscarEntidadePorId(id);

        boolean eraIndisponivel = !produto.getDisponivel();
        boolean ficaraDisponivel = request.getDisponivel() != null && request.getDisponivel();
        boolean precoReducao = request.getPreco().compareTo(produto.getPreco()) < 0;
        String precoAnterior = produto.getPreco().toString();

        produto.atualizar(
                request.getNome(),
                request.getArtista(),
                request.getPreco(),
                request.getDisponivel() != null ? request.getDisponivel() : produto.getDisponivel()
        );

        Produto atualizado = produtoRepository.save(produto);
        System.out.println("[ProdutoService] Produto atualizado: " + atualizado.getNome());

        // Observer: notifica se produto ficou disponível
        if (eraIndisponivel && ficaraDisponivel) {
            // Em produção: buscar e-mails dos clientes interessados no banco
            // Por ora, log demonstrativo
            notificacaoService.notificarProdutoDisponivel(
                    "clientes-interessados@loja.com",
                    atualizado.getNome()
            );
        }

        // Observer: notifica se houve redução de preço (promoção)
        if (precoReducao) {
            notificacaoService.notificarPromocao(
                    "clientes-interessados@loja.com",
                    atualizado.getNome(),
                    precoAnterior,
                    atualizado.getPreco().toString()
            );
        }

        return ProdutoResponseDTO.fromEntity(atualizado);
    }


    @Transactional
    public void remover(Long id) {
        Produto produto = buscarEntidadePorId(id);
        produtoRepository.delete(produto);
        System.out.println("[ProdutoService] Produto removido: " + produto.getNome());
    }

    private Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto #" + id + " não encontrado."));
    }

    private void validarRequest(ProdutoRequestDTO request) {
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }
        if (request.getArtista() == null || request.getArtista().isBlank()) {
            throw new IllegalArgumentException("Nome do artista é obrigatório.");
        }
        if (request.getPreco() == null || request.getPreco().doubleValue() <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero.");
        }
    }
}