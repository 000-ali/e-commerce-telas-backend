package com.ecommerce.telas_backend.consumer;

import java.util.Optional;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.telas_backend.dto.PedidoMensagem;
import com.ecommerce.telas_backend.model.Pedido;
import com.ecommerce.telas_backend.model.Pedido.StatusPedido;
import com.ecommerce.telas_backend.repository.PedidoRepository;
import com.ecommerce.telas_backend.service.NotificacaoService;

@Service
public class PedidoConsumer {

    private final PedidoRepository pedidoRepository;
    private final NotificacaoService notificacaoService;

    public PedidoConsumer(PedidoRepository pedidoRepository, NotificacaoService notificacaoService) {
        this.pedidoRepository = pedidoRepository;
        this.notificacaoService = notificacaoService;
    }

   
    @JmsListener(destination = "${app.queue.pedidos}")
    @Transactional
    public void receberPedido(PedidoMensagem mensagem) {
        System.out.println("[Consumer] Pedido #" + mensagem.getPedidoId() + " recebido da fila.");


        try {
             Thread.sleep(1000);
             
            // Buscar pedido no banco
            Optional<Pedido> optPedido = (Optional<Pedido>) pedidoRepository.findById(mensagem.getPedidoId());

            if (optPedido.isEmpty()) {
                System.err.println("[Consumer] Pedido #" + mensagem.getPedidoId() + " não encontrado no banco. Ignorando.");
                return;
            }

            Pedido pedido = optPedido.get();

            // Determinar novo status conforme a forma de pagamento
            StatusPedido novoStatus = determinarStatus(mensagem);

            // Atualizar e salvar o status no banco
            pedido.setStatus(novoStatus);
            pedidoRepository.save(pedido);
            System.out.println("[Consumer] Pedido #" + mensagem.getPedidoId()
                    + " atualizado para status: " + novoStatus);

            // Notificar o cliente (padrão Observer)
            notificacaoService.notificarConfirmacaoPedido(mensagem);
            notificacaoService.notificarAtualizacaoStatus(
                    mensagem.getPedidoId(), mensagem.getClienteEmail(), novoStatus);

        } catch (Exception e) {
            System.err.println("[Consumer] Erro ao processar pedido #" + mensagem.getPedidoId()
                    + ": " + e.getMessage());
            // Relançar para que o ActiveMQ recoloque na fila (dead-letter queue)
            throw new RuntimeException("Falha ao processar pedido #" + mensagem.getPedidoId(), e);
        }
    }

    
    // Determina o status inicial conforme forma de pagamento
    private StatusPedido determinarStatus(PedidoMensagem mensagem) {
        return switch (mensagem.getFormaPagamento()) {
            // Pix e Boleto ficam aguardando confirmação do gateway externo
            case PIX, BOLETO       -> StatusPedido.AGUARDANDO_PAGAMENTO;
            // Cartão é aprovado imediatamente (simulação; em produção: chamar gateway)
            case CARTAO_CREDITO    -> StatusPedido.APROVADO;
        };
    }
}