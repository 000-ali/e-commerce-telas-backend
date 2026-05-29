package com.ecommerce.telas_backend.dto;

import com.ecommerce.telas_backend.model.Carrinho;
import com.ecommerce.telas_backend.model.ItemCarrinho;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class CarrinhoDTO {

    // Request: adicionar item ao carrinho
    public static class AdicionarItemRequest {
        private Long produtoId;
        private Integer quantidade;

        public AdicionarItemRequest() {}

        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    }

    // Response: item individual do carrinho
    public static class ItemCarrinhoResponse {
        private Long id;
        private Long produtoId;
        private String nomeProduto;
        private BigDecimal precoUnitario;
        private Integer quantidade;
        private BigDecimal subtotal;

        public static ItemCarrinhoResponse fromEntity(ItemCarrinho item) {
            ItemCarrinhoResponse dto = new ItemCarrinhoResponse();
            dto.id = item.getId();
            dto.produtoId = item.getProdutoId();
            dto.nomeProduto = item.getNomeProduto();
            dto.precoUnitario = item.getPrecoUnitario();
            dto.quantidade = item.getQuantidade();
            dto.subtotal = item.calcularSubtotal();
            return dto;
        }

        public Long getId() { return id; }
        public Long getProdutoId() { return produtoId; }
        public String getNomeProduto() { return nomeProduto; }
        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public Integer getQuantidade() { return quantidade; }
        public BigDecimal getSubtotal() { return subtotal; }
    }

   
    // Response: carrinho completo com itens e total
    public static class CarrinhoResponse {
        private Long id;
        private Long clienteId;
        private List<ItemCarrinhoResponse> itens;
        private BigDecimal total;
        private int quantidadeItens;
        private LocalDateTime atualizadoEm;

        public static CarrinhoResponse fromEntity(Carrinho carrinho) {
            CarrinhoResponse dto = new CarrinhoResponse();
            dto.id = carrinho.getId();
            dto.clienteId = carrinho.getClienteId();
            dto.itens = carrinho.getItens().stream()
                    .map(ItemCarrinhoResponse::fromEntity)
                    .collect(Collectors.toList());
            dto.total = carrinho.calcularTotal();
            dto.quantidadeItens = carrinho.getItens().size();
            dto.atualizadoEm = carrinho.getAtualizadoEm();
            return dto;
        }

        public Long getId() { return id; }
        public Long getClienteId() { return clienteId; }
        public List<ItemCarrinhoResponse> getItens() { return itens; }
        public BigDecimal getTotal() { return total; }
        public int getQuantidadeItens() { return quantidadeItens; }
        public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    }
}