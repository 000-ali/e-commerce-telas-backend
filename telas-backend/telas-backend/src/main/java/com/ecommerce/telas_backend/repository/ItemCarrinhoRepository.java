package com.ecommerce.telas_backend.repository;

import com.ecommerce.telas_backend.model.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {

    // Busca um item específico dentro de um carrinho pelo produtoId
    Optional<ItemCarrinho> findByCarrinhoIdAndProdutoId(Long carrinhoId, Long produtoId);
}