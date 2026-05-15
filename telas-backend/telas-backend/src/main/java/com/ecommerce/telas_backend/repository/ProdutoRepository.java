package com.ecommerce.telas_backend.repository;

import com.ecommerce.telas_backend.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Busca por nome (contém o termo, sem diferenciar maiúsculas/minúsculas)
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // Filtra por artista (contém o nome, sem diferenciar maiúsculas/minúsculas)
    List<Produto> findByArtistaContainingIgnoreCase(String artista);

    // Lista só os produtos disponíveis
    List<Produto> findByDisponivelTrue();

    // Lista produtos por artista E disponibilidade
    List<Produto> findByArtistaContainingIgnoreCaseAndDisponivelTrue(String artista);
}