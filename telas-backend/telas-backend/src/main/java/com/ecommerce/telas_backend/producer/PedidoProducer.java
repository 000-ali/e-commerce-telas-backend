package com.ecommerce.telas_backend.producer;

import com.ecommerce.telas_backend.dto.PedidoMensagem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoProducer {

    private final JmsTemplate jmsTemplate;

    @Value("${app.queue.pedidos}")
    private String filaPedidos;

    public PedidoProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

   
    public void enviarPedido(PedidoMensagem mensagem) {
        jmsTemplate.convertAndSend(filaPedidos, mensagem);
        System.out.println("[Producer] Pedido #" + mensagem.getPedidoId()
                + " enviado para a fila '" + filaPedidos + "'");
    }
}