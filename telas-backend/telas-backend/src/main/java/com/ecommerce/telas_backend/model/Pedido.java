package com.ecommerce.telas_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data                // gera automaticamente: getters, setters, toString, equals e hashCode
@NoArgsConstructor   // gera o construtor vazio exigido pelo JPA
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
    // Construtor com os campos obrigatórios na criação do pedido.
    // O @NoArgsConstructor já cuida do construtor vazio para o JPA.
    // =========================================================
    public Pedido(Long clienteId, String clienteEmail, FormaPagamento formaPagamento, BigDecimal valorTotal) {
        this.clienteId = clienteId;
        this.clienteEmail = clienteEmail;
        this.formaPagamento = formaPagamento;
        this.valorTotal = valorTotal;
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO;
        this.criadoEm = LocalDateTime.now();
    }

    // =========================================================
    // setStatus sobrescrito para registrar atualizadoEm
    // automaticamente toda vez que o status mudar.
    // O @Data gera o setter padrão, mas este tem lógica extra.
    // =========================================================
    public void setStatus(StatusPedido status) {
        this.status = status;
        this.atualizadoEm = LocalDateTime.now();
    }
}