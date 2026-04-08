package com.ecommerce.telas_backend.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service //Diz ao Spring que essa classe é um serviço, ele cuida de criar e gerenciar ela automaticamente
public class PedidoProducer {
    private final JmsTemplate jmsTemplate; //È a ferramenta do Spring que sabe falar com o ActiveMQ. A gente não precisa configurar nada manualmente, ele já faz a conexão

    @Value("${app.queue.pedidos}") //Lê o nome da fila que configuramos no application.properties
    private String filaPedidos;
    
    public PedidoProducer(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }

    public void enviarPedido(String mensagem){
        jmsTemplate.convertAndSend(filaPedidos, mensagem);  // convertAndSend - converte a mensagem e envia para a fila
        System.out.println("Pedido enviado para a fila: " + mensagem);
    }
    
}
