package com.ecommerce.telas_backend.controller;

import com.ecommerce.telas_backend.dto.AuthDTO;
import com.ecommerce.telas_backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController — endpoints de autenticação.
 *
 *   POST /auth/cadastro  → cadastra novo usuário e retorna token JWT
 *   POST /auth/login     → autentica usuário e retorna token JWT
 *
 * Ambos são públicos (não exigem token).
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================================================
    // POST /auth/cadastro
    // Body: { "nome": "...", "email": "...", "senha": "..." }
    // =========================================================
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody AuthDTO.CadastroRequest request) {
        try {
            AuthDTO.AuthResponse response = authService.cadastrar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    // =========================================================
    // POST /auth/login
    // Body: { "email": "...", "senha": "..." }
    // =========================================================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest request) {
        try {
            AuthDTO.AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("E-mail ou senha incorretos.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao realizar login: " + e.getMessage());
        }
    }
}