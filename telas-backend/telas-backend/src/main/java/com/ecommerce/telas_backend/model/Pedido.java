package com.ecommerce.telas_backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "cliente_email", nullable = false)
    private String clienteEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> itens;

    // =========================================================
    // Enum: Status do Pedido
    // =========================================================
    public enum StatusPedido {
        AGUARDANDO_PAGAMENTO,
        APROVADO,
        RECUSADO,
        CANCELADO,
        ENTREGUE
    }

    // =========================================================
    // Enum: Forma de Pagamento (padrão Strategy)
    // =========================================================
    public enum FormaPagamento {
        PIX,
        CARTAO_CREDITO,
        BOLETO
    }

    // =========================================================
    // Construtores
    // =========================================================
    public Pedido() {}

    public Pedido(Long clienteId, String clienteEmail, FormaPagamento formaPagamento, BigDecimal valorTotal) {
        this.clienteId = clienteId;
        this.clienteEmail = clienteEmail;
        this.formaPagamento = formaPagamento;
        this.valorTotal = valorTotal;
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO;
        this.criadoEm = LocalDateTime.now();
    }

    // =========================================================
    // Getters e Setters
    // =========================================================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) {
        this.status = status;
        this.atualizadoEm = LocalDateTime.now();
    }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
}