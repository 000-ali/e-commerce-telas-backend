package com.ecommerce.telas_backend.repository;

import com.ecommerce.telas_backend.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    // Busca o carrinho de um cliente pelo clienteId
    Optional<Carrinho> findByClienteId(Long clienteId);
}