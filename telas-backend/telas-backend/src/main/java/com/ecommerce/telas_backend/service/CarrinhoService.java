package com.ecommerce.telas_backend.service;

import com.ecommerce.telas_backend.dto.CarrinhoDTO;
import com.ecommerce.telas_backend.model.Carrinho;
import com.ecommerce.telas_backend.model.ItemCarrinho;
import com.ecommerce.telas_backend.model.Produto;
import com.ecommerce.telas_backend.repository.CarrinhoRepository;
import com.ecommerce.telas_backend.repository.ItemCarrinhoRepository;
import com.ecommerce.telas_backend.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoService pedidoService;

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           ItemCarrinhoRepository itemCarrinhoRepository,
                           ProdutoRepository produtoRepository,
                           PedidoService pedidoService) {
        this.carrinhoRepository = carrinhoRepository;
        this.itemCarrinhoRepository = itemCarrinhoRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoService = pedidoService;
    }

    // Buscar ou criar carrinho do cliente
    public CarrinhoDTO.CarrinhoResponse buscarOuCriarCarrinho(Long clienteId) {
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseGet(() -> {
                    Carrinho novo = new Carrinho(clienteId);
                    return carrinhoRepository.save(novo);
                });
        return CarrinhoDTO.CarrinhoResponse.fromEntity(carrinho);
    }

    // Adicionar produto ao carrinho
    @Transactional
    public CarrinhoDTO.CarrinhoResponse adicionarItem(Long clienteId, CarrinhoDTO.AdicionarItemRequest request) {

        // Validar quantidade
        if (request.getQuantidade() == null || request.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        // Buscar produto e verificar disponibilidade
        Produto produto = produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto #" + request.getProdutoId() + " não encontrado."));

        if (!produto.getDisponivel()) {
            throw new RuntimeException("Produto '" + produto.getNome() + "' está indisponível.");
        }

        // Buscar ou criar carrinho
        Carrinho carrinho = carrinhoRepository.findByClienteId(clienteId)
                .orElseGet(() -> carrinhoRepository.save(new Carrinho(clienteId)));

        // Verificar se o produto já está no carrinho
        Optional<ItemCarrinho> itemExistente = itemCarrinhoRepository
                .findByCarrinhoIdAndProdutoId(carrinho.getId(), produto.getId());

        if (itemExistente.isPresent()) {
            // Produto já no carrinho → incrementa quantidade
            ItemCarrinho item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + request.getQuantidade());
            itemCarrinhoRepository.save(item);
            System.out.println("[CarrinhoService] Quantidade atualizada: " + produto.getNome());
        } else {
            // Produto novo → cria item
            ItemCarrinho novoItem = new ItemCarrinho(
                    carrinho,
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    request.getQuantidade()
            );
            carrinho.getItens().add(novoItem);
            System.out.println("[CarrinhoService] Item adicionado: " + produto.getNome());
        }

        carrinho.marcarAtualizado();
        carrinhoRepository.save(carrinho);

        // Recarrega o carrinho do banco para garantir dados atualizados
        Carrinho atualizado = carrinhoRepository.findByClienteId(clienteId).orElseThrow();
        return CarrinhoDTO.CarrinhoResponse.fromEntity(atualizado);
    }

    // Remover item do carrinho
    @Transactional
    public CarrinhoDTO.CarrinhoResponse removerItem(Long clienteId, Long itemId) {
        Carrinho carrinho = buscarCarrinhoPorCliente(clienteId);

        ItemCarrinho item = itemCarrinhoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item #" + itemId + " não encontrado."));

        // Garante que o item pertence ao carrinho deste cliente
        if (!item.getCarrinho().getId().equals(carrinho.getId())) {
            throw new RuntimeException("Item não pertence ao carrinho deste cliente.");
        }

        carrinho.getItens().remove(item);
        itemCarrinhoRepository.delete(item);
        carrinho.marcarAtualizado();
        carrinhoRepository.save(carrinho);

        System.out.println("[CarrinhoService] Item removido: " + item.getNomeProduto());

        Carrinho atualizado = carrinhoRepository.findByClienteId(clienteId).orElseThrow();
        return CarrinhoDTO.CarrinhoResponse.fromEntity(atualizado);
    }

    // Limpar carrinho inteiro
    @Transactional
    public void limparCarrinho(Long clienteId) {
        Carrinho carrinho = buscarCarrinhoPorCliente(clienteId);
        carrinho.getItens().clear();
        carrinho.marcarAtualizado();
        carrinhoRepository.save(carrinho);
        System.out.println("[CarrinhoService] Carrinho do cliente #" + clienteId + " limpo.");
    }

    // Finalizar compra — converte carrinho em pedido
    @Transactional
    public Object finalizarCompra(Long clienteId, String clienteEmail,
                                  com.ecommerce.telas_backend.model.Pedido.FormaPagamento formaPagamento) {

        Carrinho carrinho = buscarCarrinhoPorCliente(clienteId);

        // Bloqueia finalização se carrinho estiver vazio 
        if (carrinho.isEmpty()) {
            throw new RuntimeException("Carrinho vazio. Adicione produtos antes de finalizar.");
        }

        // Monta o PedidoRequestDTO com os itens do carrinho
        com.ecommerce.telas_backend.dto.PedidoRequestDTO pedidoRequest =
                new com.ecommerce.telas_backend.dto.PedidoRequestDTO();

        pedidoRequest.setClienteId(clienteId);
        pedidoRequest.setClienteEmail(clienteEmail);
        pedidoRequest.setFormaPagamento(formaPagamento);

        List<com.ecommerce.telas_backend.dto.PedidoRequestDTO.ItemRequestDTO> itensPedido =
                carrinho.getItens().stream().map(itemCarrinho -> {
                    com.ecommerce.telas_backend.dto.PedidoRequestDTO.ItemRequestDTO itemDTO =
                            new com.ecommerce.telas_backend.dto.PedidoRequestDTO.ItemRequestDTO();
                    itemDTO.setProdutoId(itemCarrinho.getProdutoId());
                    itemDTO.setNomeProduto(itemCarrinho.getNomeProduto());
                    itemDTO.setPrecoUnitario(itemCarrinho.getPrecoUnitario());
                    itemDTO.setQuantidade(itemCarrinho.getQuantidade());
                    return itemDTO;
                }).collect(java.util.stream.Collectors.toList());

        pedidoRequest.setItens(itensPedido);

        // Cria o pedido e envia para a fila
        com.ecommerce.telas_backend.model.Pedido pedido = pedidoService.criarPedido(pedidoRequest);

        // Limpa o carrinho após finalizar
        limparCarrinho(clienteId);

        System.out.println("[CarrinhoService] Compra finalizada! Pedido #" + pedido.getId() + " criado.");
        return pedido;
    }

    // Método interno — busca carrinho ou lança exceção
    private Carrinho buscarCarrinhoPorCliente(Long clienteId) {
        return carrinhoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RuntimeException("Carrinho do cliente #" + clienteId + " não encontrado."));
    }
}