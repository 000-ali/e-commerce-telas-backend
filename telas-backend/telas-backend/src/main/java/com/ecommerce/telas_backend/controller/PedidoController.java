package com.ecommerce.telas_backend.controller;

import com.ecommerce.telas_backend.dto.PedidoRequestDTO;
import com.ecommerce.telas_backend.model.Pedido;
import com.ecommerce.telas_backend.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PedidoController — porta de entrada da API para pedidos.
 *
 * Endpoints disponíveis:
 *   POST   /pedidos/criar            → cria pedido e envia para a fila
 *   GET    /pedidos/{id}             → busca pedido por ID
 *   GET    /pedidos/cliente/{id}     → lista pedidos de um cliente
 *
 * O Controller apenas recebe e valida a requisição; a lógica de negócio
 * fica no PedidoService.
 */
@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*") // Permite chamadas do frontend React durante desenvolvimento
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // =========================================================
    // POST /pedidos/criar
    // Frontend chama esse endpoint ao finalizar o checkout
    // =========================================================
    @PostMapping("/criar")
    public ResponseEntity<?> criarPedido(@RequestBody PedidoRequestDTO request) {
        try {
            // Validações básicas
            if (request.getItens() == null || request.getItens().isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Carrinho vazio. Adicione produtos antes de finalizar.");
            }
            if (request.getFormaPagamento() == null) {
                return ResponseEntity
                        .badRequest()
                        .body("Forma de pagamento não informada.");
            }

            Pedido pedidoCriado = pedidoService.criarPedido(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new PedidoResponseSimples(
                            pedidoCriado.getId(),
                            pedidoCriado.getStatus().name(),
                            "Pedido criado e enviado para processamento!"
                    ));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar pedido: " + e.getMessage());
        }
    }

    // =========================================================
    // GET /pedidos/{id}
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // =========================================================
    // GET /pedidos/cliente/{clienteId}
    // =========================================================
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    // =========================================================
    // Classe interna de resposta simplificada
    // =========================================================
    public record PedidoResponseSimples(Long pedidoId, String status, String mensagem) {}
}