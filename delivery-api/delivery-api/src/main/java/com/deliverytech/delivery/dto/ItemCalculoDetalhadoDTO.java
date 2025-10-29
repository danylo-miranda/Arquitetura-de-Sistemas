package com.deliverytech.delivery.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemCalculoDetalhadoDTO {
    private Long produtoId;
    private String produtoNome;
    private String produtoDescricao;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
    private boolean disponivel;

    public ItemCalculoDetalhadoDTO() {}

    public ItemCalculoDetalhadoDTO(Long produtoId, String produtoNome, String produtoDescricao, 
                                  Integer quantidade, BigDecimal precoUnitario, BigDecimal subtotal, 
                                  boolean disponivel) {
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.produtoDescricao = produtoDescricao;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = subtotal;
        this.disponivel = disponivel;
    }
}
