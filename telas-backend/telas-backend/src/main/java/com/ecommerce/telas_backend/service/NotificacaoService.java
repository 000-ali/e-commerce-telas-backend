package com.ecommerce.telas_backend.service;

import com.ecommerce.telas_backend.dto.PedidoMensagem;
import com.ecommerce.telas_backend.model.Pedido.StatusPedido;
import org.springframework.stereotype.Service;

/**
 * NotificacaoService — implementa o padrão Observer.
 *
 * Responsável por notificar o cliente após mudanças no status do pedido.
 * Aqui está o ponto de integração com e-mail / push notification.
 *
 * Por enquanto usa log no console. Para integrar com e-mail real,
 * basta injetar JavaMailSender (Spring Mail) e substituir os System.out.
 */
@Service
public class NotificacaoService {

    /**
     * Notifica o cliente sobre a confirmação do pedido.
     * Chamado pelo Consumer quando o pedido é salvo com sucesso.
     */
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

    /**
     * Notifica o cliente sobre atualização de status do pedido.
     * Ex: AGUARDANDO_PAGAMENTO → APROVADO
     */
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

    /**
     * Notifica clientes interessados quando um produto fica disponível.
     * Chamado pelo serviço de produtos quando Admin altera disponibilidade.
     */
    public void notificarProdutoDisponivel(String clienteEmail, String nomeProduto) {
        String assunto = "Produto disponível: " + nomeProduto;
        String corpo = "O produto '" + nomeProduto + "' que você acompanhava está disponível novamente!";
        enviarEmail(clienteEmail, assunto, corpo);
    }

    /**
     * Notifica clientes sobre promoção em produto.
     */
    public void notificarPromocao(String clienteEmail, String nomeProduto, String precoAntigo, String precoNovo) {
        String assunto = "Promoção! " + nomeProduto + " com desconto";
        String corpo = String.format(
                "O produto '%s' está em promoção!\nDe: R$ %s  Por: R$ %s\nCorra antes que acabe!",
                nomeProduto, precoAntigo, precoNovo
        );
        enviarEmail(clienteEmail, assunto, corpo);
    }

    // =========================================================
    // Método interno de envio de e-mail
    // Para usar e-mail real: injete JavaMailSender aqui
    // =========================================================
    private void enviarEmail(String destinatario, String assunto, String corpo) {
        // TODO: substituir por JavaMailSender quando configurar SMTP
        System.out.println("=========================================");
        System.out.println("[Notificação] Para: " + destinatario);
        System.out.println("[Notificação] Assunto: " + assunto);
        System.out.println("[Notificação] Corpo: " + corpo);
        System.out.println("=========================================");
    }
}