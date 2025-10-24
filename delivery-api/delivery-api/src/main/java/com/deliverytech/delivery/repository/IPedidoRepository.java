package com.deliverytech.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.StatusPedido;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
    
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    
    List<Pedido> findByClienteId(Long clienteId);
    
    List<Pedido> findByRestauranteId(Long restauranteId);
    
    List<Pedido> findByStatus(StatusPedido status);
    
    boolean existsByNumeroPedido(String numeroPedido);
    
    @Query("SELECT COUNT(p) > 0 FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status IN :statuses")
    boolean existsByClienteIdAndStatusIn(@Param("clienteId") Long clienteId, @Param("statuses") List<StatusPedido> statuses);
    
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId AND p.status = :status")
    List<Pedido> findByRestauranteIdAndStatus(@Param("restauranteId") Long restauranteId, @Param("status") StatusPedido status);
}