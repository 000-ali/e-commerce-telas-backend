package com.ecommerce.telas_backend.repository;

import com.ecommerce.telas_backend.model.Pedido;
import com.ecommerce.telas_backend.model.Pedido.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Busca todos os pedidos de um cliente específico
    List<Pedido> findByClienteIdOrderByCriadoEmDesc(Long clienteId);

    // Busca pedidos por status (ex: todos AGUARDANDO_PAGAMENTO)
    List<Pedido> findByStatus(StatusPedido status);

    // Busca pedidos por e-mail do cliente
    List<Pedido> findByClienteEmailOrderByCriadoEmDesc(String clienteEmail);

    Optional<Pedido> findById(Long id);
}