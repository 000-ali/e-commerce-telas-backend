package com.ecommerce.telas_backend.service;

import com.ecommerce.telas_backend.dto.PedidoMensagem;
import com.ecommerce.telas_backend.dto.PedidoRequestDTO;
import com.ecommerce.telas_backend.model.ItemPedido;
import com.ecommerce.telas_backend.model.Pedido;
import com.ecommerce.telas_backend.producer.PedidoProducer;
import com.ecommerce.telas_backend.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoProducer pedidoProducer;

    public PedidoService(PedidoRepository pedidoRepository, PedidoProducer pedidoProducer) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoProducer = pedidoProducer;
    }

    /**
     * Cria o pedido no banco e publica na fila.
     * @return o Pedido salvo com ID gerado
     */
    @Transactional (propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public Pedido criarPedido(PedidoRequestDTO request) {

        // Calcular valor total somando precoUnitario * quantidade de cada item
        BigDecimal valorTotal = request.getItens().stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Criar entidade Pedido
        Pedido pedido = new Pedido(
                request.getClienteId(),
                request.getClienteEmail(),
                request.getFormaPagamento(),
                valorTotal
        );

        // Criar e vincular os itens ao pedido
        List<ItemPedido> itens = request.getItens().stream().map(itemDTO -> {
            ItemPedido item = new ItemPedido(
                    itemDTO.getProdutoId(),
                    itemDTO.getNomeProduto(),
                    itemDTO.getPrecoUnitario(),
                    itemDTO.getQuantidade()
            );
            item.setPedido(pedido);
            return item;
        }).collect(Collectors.toList());

        pedido.setItens(itens);

        // Salvar no banco
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        System.out.println("[Service] Pedido #" + pedidoSalvo.getId() + " salvo no banco.");

        // Montar mensagem e publicar na fila
        PedidoMensagem mensagem = montarMensagem(pedidoSalvo, request);
        pedidoProducer.enviarPedido(mensagem);

        return pedidoSalvo;
    }


    public List<Pedido> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdOrderByCriadoEmDesc(clienteId);
    }


    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido #" + id + " não encontrado."));
    }

    private PedidoMensagem montarMensagem(Pedido pedido, PedidoRequestDTO request) {
        List<PedidoMensagem.ItemMensagem> itensMensagem = request.getItens().stream()
                .map(i -> new PedidoMensagem.ItemMensagem(
                        i.getProdutoId(),
                        i.getNomeProduto(),
                        i.getPrecoUnitario(),
                        i.getQuantidade()))
                .collect(Collectors.toList());

        return new PedidoMensagem(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getClienteEmail(),
                pedido.getFormaPagamento(),
                pedido.getValorTotal(),
                itensMensagem
        );
    }
}