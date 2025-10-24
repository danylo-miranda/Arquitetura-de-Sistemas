package com.deliverytech.delivery.dto;

import lombok.Data;

@Data
public class StatusPedidoDTO {
    private String status;
    private String descricao;

    public StatusPedidoDTO(String status, String descricao) {
        this.status = status;
        this.descricao = descricao;
    }
}