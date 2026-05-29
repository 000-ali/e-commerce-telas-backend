package com.ecommerce.telas_backend.controller;

import com.ecommerce.telas_backend.dto.CarrinhoDTO;
import com.ecommerce.telas_backend.dto.FinalizarCompraRequest;
import com.ecommerce.telas_backend.service.CarrinhoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
@CrossOrigin(origins = "*")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    // GET /carrinho/{clienteId} - visualizar carrinho
    @GetMapping("/{clienteId}")
    public ResponseEntity<?> verCarrinho(@PathVariable Long clienteId) {
        try {
            CarrinhoDTO.CarrinhoResponse carrinho = carrinhoService.buscarOuCriarCarrinho(clienteId);
            return ResponseEntity.ok(carrinho);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar carrinho: " + e.getMessage());
        }
    }

    // POST /carrinho/{clienteId}/adicionar - adicionar produto
    // Body: { "produtoId": 1, "quantidade": 1 }
    @PostMapping("/{clienteId}/adicionar")
    public ResponseEntity<?> adicionarItem(@PathVariable Long clienteId,
                                           @RequestBody CarrinhoDTO.AdicionarItemRequest request) {
        try {
            CarrinhoDTO.CarrinhoResponse carrinho = carrinhoService.adicionarItem(clienteId, request);
            return ResponseEntity.ok(carrinho);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar item: " + e.getMessage());
        }
    }


    // DELETE /carrinho/{clienteId}/item/{itemId} - remover item
    @DeleteMapping("/{clienteId}/item/{itemId}")
    public ResponseEntity<?> removerItem(@PathVariable Long clienteId,
                                         @PathVariable Long itemId) {
        try {
            CarrinhoDTO.CarrinhoResponse carrinho = carrinhoService.removerItem(clienteId, itemId);
            return ResponseEntity.ok(carrinho);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover item: " + e.getMessage());
        }
    }


    // DELETE /carrinho/{clienteId}/limpar - limpar carrinho
    @DeleteMapping("/{clienteId}/limpar")
    public ResponseEntity<?> limparCarrinho(@PathVariable Long clienteId) {
        try {
            carrinhoService.limparCarrinho(clienteId);
            return ResponseEntity.ok("Carrinho limpo com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao limpar carrinho: " + e.getMessage());
        }
    }


    // POST /carrinho/{clienteId}/finalizar - finalizar compra
    // Body: { "clienteEmail": "...", "formaPagamento": "PIX" }
  @PostMapping("/{clienteId}/finalizar")
public ResponseEntity<?> finalizarCompra(@PathVariable Long clienteId,
                                         @RequestBody FinalizarCompraRequest request) {
    try {
        com.ecommerce.telas_backend.model.Pedido pedido =
                (com.ecommerce.telas_backend.model.Pedido) carrinhoService.finalizarCompra(
                clienteId,
                request.getClienteEmail(),
                request.getFormaPagamento()
        );

        // Retorna só os campos essenciais, sem referências circulares
        java.util.Map<String, Object> resposta = new java.util.HashMap<>();
        resposta.put("pedidoId", pedido.getId());
        resposta.put("status", pedido.getStatus());
        resposta.put("formaPagamento", pedido.getFormaPagamento());
        resposta.put("valorTotal", pedido.getValorTotal());
        resposta.put("mensagem", "Pedido criado e enviado para processamento!");

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao finalizar compra: " + e.getMessage());
    }
}
}