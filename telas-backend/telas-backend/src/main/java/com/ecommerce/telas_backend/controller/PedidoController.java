package com.ecommerce.telas_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.telas_backend.producer.PedidoProducer;

@RestController // Classe que recebe requisições HTTP. Porta de entrada da API
@RequestMapping("/pedidos") // Quando o frontend chamar esse endereço, o pedido vai para a fila
public class PedidoController {

    private final PedidoProducer pedidoProducer;

    public PedidoController(PedidoProducer pedidoProducer){
        this.pedidoProducer = pedidoProducer;
    }

    @PostMapping("/enviar") // Quando o frontend chamar esse endereço, o pedido vai para a fila
    public String enviarPedido(@RequestBody String mensagem){
        pedidoProducer.enviarPedido(mensagem);
        return "Pedido enviado para a fila com sucesso!";
    }
    
    
}
