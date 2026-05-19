package com.ecommerce.telas_backend.service;

import com.ecommerce.telas_backend.dto.AuthDTO;
import com.ecommerce.telas_backend.model.Usuario;
import com.ecommerce.telas_backend.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService — lógica de cadastro e login.
 *
 * Cadastro:
 *   1. Verifica se e-mail já existe
 *   2. Criptografa a senha com BCrypt
 *   3. Salva o usuário com perfil CLIENTE
 *   4. Gera e retorna o token JWT
 *
 * Login:
 *   1. AuthenticationManager valida e-mail e senha
 *   2. Carrega o usuário do banco
 *   3. Gera e retorna o token JWT
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // =========================================================
    // Cadastro de novo usuário (perfil padrão: CLIENTE)
    // =========================================================
    public AuthDTO.AuthResponse cadastrar(AuthDTO.CadastroRequest request) {

        // Verifica se e-mail já está em uso
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        // Cria usuário com senha criptografada
        Usuario usuario = new Usuario(
                request.getNome(),
                request.getEmail(),
                passwordEncoder.encode(request.getSenha()),
                Usuario.Perfil.CLIENTE
        );

        usuarioRepository.save(usuario);
        System.out.println("[AuthService] Novo usuário cadastrado: " + usuario.getEmail());

        // Gera token JWT
        String token = jwtService.gerarToken(usuario);

        return new AuthDTO.AuthResponse(
                token,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name()
        );
    }

    // =========================================================
    // Login
    // =========================================================
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {

        // Valida e-mail e senha (lança exceção se inválido)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        // Carrega o usuário do banco
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        System.out.println("[AuthService] Login realizado: " + usuario.getEmail());

        // Gera token JWT
        String token = jwtService.gerarToken(usuario);

        return new AuthDTO.AuthResponse(
                token,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name()
        );
    }
}