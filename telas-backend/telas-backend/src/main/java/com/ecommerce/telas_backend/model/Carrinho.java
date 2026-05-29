package com.ecommerce.telas_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "carrinhos")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Um carrinho pertence a um cliente (por enquanto só o ID, sem entidade Cliente ainda)
    @Column(name = "cliente_id", nullable = false, unique = true)
    private Long clienteId;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemCarrinho> itens = new ArrayList<>();

    public Carrinho(Long clienteId) {
        this.clienteId = clienteId;
        this.criadoEm = LocalDateTime.now();
    }

  
    public BigDecimal calcularTotal() {
        return itens.stream()
                .map(item -> item.getPrecoUnitario()
                        .multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return itens == null || itens.isEmpty();
    }

   
    public void marcarAtualizado() {
        this.atualizadoEm = LocalDateTime.now();
    }
}