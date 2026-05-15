package com.ecommerce.telas_backend.controller;

import com.ecommerce.telas_backend.dto.ProdutoRequestDTO;
import com.ecommerce.telas_backend.dto.ProdutoResponseDTO;
import com.ecommerce.telas_backend.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProdutoController — endpoints REST do catálogo de pinturas.
 *
 * Endpoints públicos (cliente pode acessar):
 *   GET  /produtos                     → lista todos os disponíveis
 *   GET  /produtos/{id}                → detalhe de uma pintura
 *   GET  /produtos/buscar?nome=...     → pesquisa por nome da obra
 *   GET  /produtos/artista?nome=...    → filtra por artista
 *
 * Endpoints admin (somente Administrador — controle via Spring Security futuramente):
 *   POST   /produtos/admin             → cadastrar nova pintura
 *   PUT    /produtos/admin/{id}        → editar pintura
 *   DELETE /produtos/admin/{id}        → remover pintura
 *   GET    /produtos/admin/todos       → lista todos (incluindo indisponíveis)
 */
@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // =========================================================
    // PÚBLICOS — catálogo do cliente
    // =========================================================

    // GET /produtos → lista pinturas disponíveis
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarDisponiveis() {
        return ResponseEntity.ok(produtoService.listarDisponiveis());
    }

    // GET /produtos/{id} → detalhe de uma pintura
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(produtoService.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET /produtos/buscar?nome=monalisa → pesquisa por nome
    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    // GET /produtos/artista?nome=van+gogh → filtra por artista
    @GetMapping("/artista")
    public ResponseEntity<List<ProdutoResponseDTO>> filtrarPorArtista(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.filtrarPorArtista(nome));
    }

    // =========================================================
    // ADMIN — gerenciamento do catálogo
    // =========================================================

    // GET /produtos/admin/todos → lista todos (incluindo indisponíveis)
    @GetMapping("/admin/todos")
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    // POST /produtos/admin → cadastrar nova pintura
    @PostMapping("/admin")
    public ResponseEntity<?> criar(@RequestBody ProdutoRequestDTO request) {
        try {
            ProdutoResponseDTO criado = produtoService.criar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    // PUT /produtos/admin/{id} → editar pintura
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody ProdutoRequestDTO request) {
        try {
            ProdutoResponseDTO atualizado = produtoService.editar(id, request);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao editar produto: " + e.getMessage());
        }
    }

    // DELETE /produtos/admin/{id} → remover pintura
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        try {
            produtoService.remover(id);
            return ResponseEntity.ok("Produto removido com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover produto: " + e.getMessage());
        }
    }
}