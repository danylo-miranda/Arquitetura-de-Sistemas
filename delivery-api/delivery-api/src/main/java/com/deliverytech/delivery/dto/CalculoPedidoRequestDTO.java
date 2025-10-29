package com.deliverytech.delivery.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalculoPedidoRequestDTO {
    
    @NotNull(message = "Restaurante é obrigatório")
    private Long restauranteId;
    
    @NotEmpty(message = "Itens são obrigatórios")
    private List<ItemCalculoDTO> itens;

    @Data
    public static class ItemCalculoDTO {
        @NotNull(message = "Produto é obrigatório")
        private Long produtoId;
        
        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
        private Integer quantidade;
    }
}