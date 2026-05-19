package com.ecommerce.telas_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig — define as regras de acesso de toda a API.
 *
 * Rotas públicas (sem token):
 *   POST /auth/login      → qualquer um pode fazer login
 *   POST /auth/cadastro   → qualquer um pode se cadastrar
 *   GET  /produtos        → catálogo público
 *   GET  /produtos/{id}   → detalhe público
 *   GET  /produtos/buscar → pesquisa pública
 *   GET  /produtos/artista → filtro público
 *
 * Rotas de Admin (exige token + perfil ADMINISTRADOR):
 *   qualquer /produtos/admin/**
 *
 * Rotas autenticadas (exige token, qualquer perfil):
 *   /carrinho/**
 *   /pedidos/**
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // Rotas públicas de autenticação
                .requestMatchers("/auth/login", "/auth/cadastro").permitAll()

                // Catálogo público (cliente não autenticado pode ver)
                .requestMatchers(HttpMethod.GET, "/produtos").permitAll()
                .requestMatchers(HttpMethod.GET, "/produtos/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/produtos/buscar").permitAll()
                .requestMatchers(HttpMethod.GET, "/produtos/artista").permitAll()

                // Rotas de admin — somente ADMINISTRADOR (RNF004)
                .requestMatchers("/produtos/admin/**").hasRole("ADMINISTRADOR")

                // Carrinho e pedidos — qualquer usuário autenticado
                .requestMatchers("/carrinho/**").authenticated()
                .requestMatchers("/pedidos/**").authenticated()

                // Qualquer outra rota exige autenticação
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}