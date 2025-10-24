package com.deliverytech.delivery.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemPedidoResponseDTO {
    private Long produtoId;
    private String produtoNome;
    private String produtoDescricao;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    // Construtor vazio
    public ItemPedidoResponseDTO() {}

    // Construtor com campos principais
    public ItemPedidoResponseDTO(Long produtoId, String produtoNome, Integer quantidade, 
                               BigDecimal precoUnitario, BigDecimal subtotal) {
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
    }

    // Construtor completo
    public ItemPedidoResponseDTO(Long produtoId, String produtoNome, String produtoDescricao, 
                               Integer quantidade, BigDecimal precoUnitario, BigDecimal subtotal) {
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.produtoDescricao = produtoDescricao;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
    }
}
