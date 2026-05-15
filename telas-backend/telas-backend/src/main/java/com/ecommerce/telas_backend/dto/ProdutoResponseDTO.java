package com.ecommerce.telas_backend.dto;

import com.ecommerce.telas_backend.model.Produto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO enviado ao Frontend com os dados do produto.
 * Evita expor a entidade JPA diretamente na API.
 */
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String artista;
    private BigDecimal preco;
    private Boolean disponivel;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    // =========================================================
    // Converte entidade Produto para DTO de resposta
    // =========================================================
    public static ProdutoResponseDTO fromEntity(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.id = produto.getId();
        dto.nome = produto.getNome();
        dto.artista = produto.getArtista();
        dto.preco = produto.getPreco();
        dto.disponivel = produto.getDisponivel();
        dto.criadoEm = produto.getCriadoEm();
        dto.atualizadoEm = produto.getAtualizadoEm();
        return dto;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getArtista() { return artista; }
    public BigDecimal getPreco() { return preco; }
    public Boolean getDisponivel() { return disponivel; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}