package com.deliverytech.delivery.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CalculoPedidoResponseDTO {
    private BigDecimal valorSubtotal;
    private BigDecimal taxaEntrega;
    private BigDecimal valorTotal;
    private Integer quantidadeItens;
    private String restauranteNome;
    private String mensagem;

    public CalculoPedidoResponseDTO() {}

    public CalculoPedidoResponseDTO(BigDecimal valorSubtotal, BigDecimal taxaEntrega, 
                                   BigDecimal valorTotal, Integer quantidadeItens, 
                                   String restauranteNome) {
        this.valorSubtotal = valorSubtotal;
        this.taxaEntrega = taxaEntrega;
        this.valorTotal = valorTotal;
        this.quantidadeItens = quantidadeItens;
        this.restauranteNome = restauranteNome;
    }
}