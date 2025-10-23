package com.deliverytech.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Restaurante;;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
    
    // Verifica se existe algum pedido para o restaurante
    boolean existsByRestaurante(Restaurante restaurante);
    
    // Ou se quiser verificar por ID do restaurante
    boolean existsByRestauranteId(Long restauranteId);
}