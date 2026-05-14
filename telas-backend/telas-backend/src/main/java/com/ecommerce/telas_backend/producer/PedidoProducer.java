package com.ecommerce.telas_backend.producer;

import com.ecommerce.telas_backend.dto.PedidoMensagem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * PedidoProducer — responsável por PUBLICAR pedidos na fila do ActiveMQ.
 *
 * Fluxo:
 *   PedidoService cria o pedido no banco → chama Producer → mensagem vai para a fila
 *   → Consumer pega da fila → processa (status + notificação)
 *
 * A mensagem é serializada automaticamente como JSON pelo Jackson
 * (configurado em ActiveMQConfig).
 */
@Service
public class PedidoProducer {

    private final JmsTemplate jmsTemplate;

    @Value("${app.queue.pedidos}")
    private String filaPedidos;

    public PedidoProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    /**
     * Publica um pedido na fila. O objeto PedidoMensagem é convertido para JSON
     * automaticamente pelo MappingJackson2MessageConverter.
     */
    public void enviarPedido(PedidoMensagem mensagem) {
        jmsTemplate.convertAndSend(filaPedidos, mensagem);
        System.out.println("[Producer] Pedido #" + mensagem.getPedidoId()
                + " enviado para a fila '" + filaPedidos + "'");
    }
}