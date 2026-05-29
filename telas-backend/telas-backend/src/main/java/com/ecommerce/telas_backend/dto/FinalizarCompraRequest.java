package com.ecommerce.telas_backend.dto;

import com.ecommerce.telas_backend.model.Pedido.FormaPagamento;


public class FinalizarCompraRequest {

    private String clienteEmail;
    private FormaPagamento formaPagamento;

    public FinalizarCompraRequest() {}

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
}