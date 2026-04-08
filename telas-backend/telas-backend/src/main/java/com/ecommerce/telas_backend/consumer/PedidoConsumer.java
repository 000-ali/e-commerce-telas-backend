package com.ecommerce.telas_backend.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class PedidoConsumer {

    @JmsListener(destination = "${app.queue.pedidos}") // Fica escutando a fila o tempo todo. Assim que chegar a mensagem, esse metodo é chamado automaticamente
    public void receberPedido(String mensagem){
        System.out.println("Pedido recebido da fila: " + mensagem);
        System.out.println("Processando pedido...");
    }
    
}
