package com.ecommerce.telas_backend.dto;

import com.ecommerce.telas_backend.model.Pedido.FormaPagamento;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO que representa o pedido serializado em JSON dentro da fila ActiveMQ.
 * É o objeto que "viaja" entre o Producer e o Consumer.
 */
public class PedidoMensagem {

    private Long pedidoId;
    private Long clienteId;
    private String clienteEmail;
    private FormaPagamento formaPagamento;
    private BigDecimal valorTotal;
    private List<ItemMensagem> itens;

    // =========================================================
    // Classe interna: item do pedido dentro da mensagem
    // =========================================================
    public static class ItemMensagem {
        private Long produtoId;
        private String nomeProduto;
        private BigDecimal precoUnitario;
        private Integer quantidade;

        public ItemMensagem() {}

        public ItemMensagem(Long produtoId, String nomeProduto, BigDecimal precoUnitario, Integer quantidade) {
            this.produtoId = produtoId;
            this.nomeProduto = nomeProduto;
            this.precoUnitario = precoUnitario;
            this.quantidade = quantidade;
        }

        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

        public String getNomeProduto() { return nomeProduto; }
        public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    }

    // =========================================================
    // Construtores
    // =========================================================
    public PedidoMensagem() {}

    public PedidoMensagem(Long pedidoId, Long clienteId, String clienteEmail,
                          FormaPagamento formaPagamento, BigDecimal valorTotal,
                          List<ItemMensagem> itens) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.clienteEmail = clienteEmail;
        this.formaPagamento = formaPagamento;
        this.valorTotal = valorTotal;
        this.itens = itens;
    }

    // =========================================================
    // Getters e Setters
    // =========================================================
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public List<ItemMensagem> getItens() { return itens; }
    public void setItens(List<ItemMensagem> itens) { this.itens = itens; }
}