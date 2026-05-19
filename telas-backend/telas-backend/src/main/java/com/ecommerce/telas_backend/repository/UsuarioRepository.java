package com.ecommerce.telas_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.telas_backend.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca usúario pelo e-mail (usado no login)

    Optional<Usuario> findByEmail (String email);

    // Verifica se e-mail já está cadastrado

    boolean existsByEmail(String email);
    
}
