package com.deliverytech.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Pedido;

@Repository
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
    public Pedido findByNome(String nome);
    public boolean existsByNome(String nome);
}
