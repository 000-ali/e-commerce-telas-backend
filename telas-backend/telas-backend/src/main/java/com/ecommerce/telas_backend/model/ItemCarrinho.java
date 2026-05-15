package com.ecommerce.telas_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@NoArgsConstructor
@Entity
@Table(name = "itens_carrinho")
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    @Column(name = "produto_id", nullable = false)
    private Long produtoId;

    @Column(name = "nome_produto", nullable = false)
    private String nomeProduto;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private Integer quantidade;

    // =========================================================
    // Construtor com campos obrigatórios
    // =========================================================
    public ItemCarrinho(Carrinho carrinho, Long produtoId, String nomeProduto,
                        BigDecimal precoUnitario, Integer quantidade) {
        this.carrinho = carrinho;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    // =========================================================
    // Calcula subtotal deste item (precoUnitario * quantidade)
    // =========================================================
    public BigDecimal calcularSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}