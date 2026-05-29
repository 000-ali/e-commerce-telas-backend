package com.ecommerce.telas_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String artista;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean disponivel = true;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public Produto(String nome, String artista, BigDecimal preco) {
        this.nome = nome;
        this.artista = artista;
        this.preco = preco;
        this.disponivel = true;
        this.criadoEm = LocalDateTime.now();
    }

   
    public void atualizar(String nome, String artista, BigDecimal preco, Boolean disponivel) {
        this.nome = nome;
        this.artista = artista;
        this.preco = preco;
        this.disponivel = disponivel;
        this.atualizadoEm = LocalDateTime.now();
    }
}