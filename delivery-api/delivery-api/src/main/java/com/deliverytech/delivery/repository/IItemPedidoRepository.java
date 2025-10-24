package com.deliverytech.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.ItemPedido;

@Repository
public interface IItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}