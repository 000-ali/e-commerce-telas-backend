package com.ecommerce.telas_backend.service;

import com.ecommerce.telas_backend.dto.PedidoMensagem;
import com.ecommerce.telas_backend.model.Pedido.StatusPedido;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    public void notificarConfirmacaoPedido(PedidoMensagem mensagem) {
        String destinatario = mensagem.getClienteEmail();
        String assunto = "Pedido #" + mensagem.getPedidoId() + " recebido!";
        String corpo = String.format(
                "Olá! Seu pedido #%d foi recebido com sucesso.\n" +
                "Forma de pagamento: %s\n" +
                "Valor total: R$ %.2f\n" +
                "Acompanhe o status pelo nosso site.",
                mensagem.getPedidoId(),
                mensagem.getFormaPagamento(),
                mensagem.getValorTotal()
        );

        enviarEmail(destinatario, assunto, corpo);
    }

    public void notificarAtualizacaoStatus(Long pedidoId, String clienteEmail, StatusPedido novoStatus) {
        String mensagemStatus = switch (novoStatus) {
            case APROVADO          -> "Seu pagamento foi aprovado! Obrigada pela compra.";
            case RECUSADO          -> "Seu pagamento foi recusado. Tente novamente ou use outra forma.";
            case CANCELADO         -> "Seu pedido foi cancelado.";
            case ENTREGUE          -> "Sua obra foi entregue! Esperamos que você ame sua pintura.";
            default                -> "Seu pedido foi atualizado para: " + novoStatus;
        };

        String assunto = "Atualização do Pedido #" + pedidoId;
        enviarEmail(clienteEmail, assunto, mensagemStatus);
    }


    public void notificarProdutoDisponivel(String clienteEmail, String nomeProduto) {
        String assunto = "Produto disponível: " + nomeProduto;
        String corpo = "O produto '" + nomeProduto + "' que você acompanhava está disponível novamente!";
        enviarEmail(clienteEmail, assunto, corpo);
    }

 
    public void notificarPromocao(String clienteEmail, String nomeProduto, String precoAntigo, String precoNovo) {
        String assunto = "Promoção! " + nomeProduto + " com desconto";
        String corpo = String.format(
                "O produto '%s' está em promoção!\nDe: R$ %s  Por: R$ %s\nCorra antes que acabe!",
                nomeProduto, precoAntigo, precoNovo
        );
        enviarEmail(clienteEmail, assunto, corpo);
    }

 
    private void enviarEmail(String destinatario, String assunto, String corpo) {
        // TODO: substituir por JavaMailSender quando configurar SMTP
        System.out.println("=========================================");
        System.out.println("[Notificação] Para: " + destinatario);
        System.out.println("[Notificação] Assunto: " + assunto);
        System.out.println("[Notificação] Corpo: " + corpo);
        System.out.println("=========================================");
    }
}