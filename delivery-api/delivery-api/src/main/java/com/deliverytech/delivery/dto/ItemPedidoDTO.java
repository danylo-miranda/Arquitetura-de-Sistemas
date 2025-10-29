package com.deliverytech.delivery.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemPedidoDTO {
 
    @NotNull(message = "Produto é obrigatório")
    private Long produtoId;
 
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    @Max(value = 10, message = "Quantidade máxima é 10")
    private Integer quantidade;
 
    // Campos de resposta (não são preenchidos na requisição)
    private String produtoNome;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
 
    // Construtor padrão
    public ItemPedidoDTO() {}
 
    // Construtor para facilitar testes
    public ItemPedidoDTO(Long produtoId, Integer quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }
}