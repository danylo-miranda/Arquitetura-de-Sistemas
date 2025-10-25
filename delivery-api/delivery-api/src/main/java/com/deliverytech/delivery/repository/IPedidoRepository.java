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
    
    // Buscar por número do pedido
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    
    // Buscar por ID do cliente
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId")
    List<Pedido> findByClienteId(@Param("clienteId") Long clienteId);
    
    // Buscar por ID do restaurante
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId")
    List<Pedido> findByRestauranteId(@Param("restauranteId") Long restauranteId);
    
    // Buscar por status
    @Query("SELECT p FROM Pedido p WHERE p.status = :status")
    List<Pedido> findByStatus(@Param("status") StatusPedido status);
    
    // Verificar se existe pedido com número
    boolean existsByNumeroPedido(String numeroPedido);
    
    // Buscar por cliente e status
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status = :status")
    List<Pedido> findByClienteIdAndStatus(@Param("clienteId") Long clienteId, @Param("status") StatusPedido status);
    
    // Buscar por restaurante e status
    @Query("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId AND p.status = :status")
    List<Pedido> findByRestauranteIdAndStatus(@Param("restauranteId") Long restauranteId, @Param("status") StatusPedido status);
}