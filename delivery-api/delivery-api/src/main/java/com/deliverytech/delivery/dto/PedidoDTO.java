package com.deliverytech.delivery.dto;

import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Restaurant;
import com.deliverytech.delivery.entity.ItemPedido;

import lombok.Data;
import java.time.OffsetDateTime;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PedidoDTO {
    private Long id;
    private OffsetDateTime dataPedido;
    private String enderecoEntrega;
    private BigDecimal subtotal;
    private BigDecimal taxaEntrega;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private Cliente cliente;
    private Restaurant restaurante;
    private List<ItemPedido> itens;
}
