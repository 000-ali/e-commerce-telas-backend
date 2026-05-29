package com.ecommerce.telas_backend.config;

import com.ecommerce.telas_backend.model.Usuario;
import com.ecommerce.telas_backend.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Verifica se já existe um Admin no banco
        boolean adminExiste = usuarioRepository.findAll()
                .stream()
                .anyMatch(u -> u.getPerfil() == Usuario.Perfil.ADMINISTRADOR);

        if (!adminExiste) {
            Usuario admin = new Usuario(
                    "Administrador",
                    "admin@telas.com",
                    passwordEncoder.encode("admin123"),
                    Usuario.Perfil.ADMINISTRADOR
            );
            usuarioRepository.save(admin);
            System.out.println("=========================================");
            System.out.println("[DataInitializer] Admin criado com sucesso!");
            System.out.println("[DataInitializer] E-mail: admin@telas.com");
            System.out.println("[DataInitializer] Senha:  admin123");
            System.out.println("=========================================");
        } else {
            System.out.println("[DataInitializer] Admin já existe no banco. Nenhuma ação necessária.");
        }
    }
}