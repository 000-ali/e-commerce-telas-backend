package com.ecommerce.telas_backend.dto;

import com.ecommerce.telas_backend.model.Pedido.FormaPagamento;
import java.math.BigDecimal;
import java.util.List;


public class PedidoRequestDTO {

    private Long clienteId;
    private String clienteEmail;
    private FormaPagamento formaPagamento;
    private List<ItemRequestDTO> itens;

    public static class ItemRequestDTO {
        private Long produtoId;
        private String nomeProduto;
        private BigDecimal precoUnitario;
        private Integer quantidade;

        public ItemRequestDTO() {}

        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

        public String getNomeProduto() { return nomeProduto; }
        public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public List<ItemRequestDTO> getItens() { return itens; }
    public void setItens(List<ItemRequestDTO> itens) { this.itens = itens; }
}