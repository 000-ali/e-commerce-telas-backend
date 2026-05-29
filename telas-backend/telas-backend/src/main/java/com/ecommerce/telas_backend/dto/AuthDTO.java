package com.ecommerce.telas_backend.dto;

public class AuthDTO {

    // Request: cadastro de novo usuário
    public static class CadastroRequest {
        private String nome;
        private String email;
        private String senha;

        public CadastroRequest() {}

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    // Request: login
    public static class LoginRequest {
        private String email;
        private String senha;

        public LoginRequest() {}

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }

    // Response: retorno após login ou cadastro com sucesso
   public static class AuthResponse {
    private Long id;
    private String token;
    private String nome;
    private String email;
    private String perfil;

    public AuthResponse(Long id, String token, String nome, String email, String perfil) {
        this.id = id;
        this.token = token;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getPerfil() { return perfil; }
}
}