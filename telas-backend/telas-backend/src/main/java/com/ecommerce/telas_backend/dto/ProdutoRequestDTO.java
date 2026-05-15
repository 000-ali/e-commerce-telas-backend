package com.ecommerce.telas_backend.dto;

import java.math.BigDecimal;

/**
 * DTO recebido do Frontend para criar ou editar um produto.
 */
public class ProdutoRequestDTO {

    private String nome;
    private String artista;
    private BigDecimal preco;
    private Boolean disponivel;

    public ProdutoRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }
}