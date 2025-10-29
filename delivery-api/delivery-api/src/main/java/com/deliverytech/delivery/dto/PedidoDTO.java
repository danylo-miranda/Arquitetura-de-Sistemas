package com.deliverytech.delivery.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PedidoDTO {
    private Long id;
    
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    
    @NotNull(message = "Restaurante é obrigatório")
    private Long restauranteId;
    
    @NotBlank(message = "Endereço de entrega é obrigatório")
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String enderecoEntrega;
    
    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    private List<ItemPedidoDTO> itens;
    
    // Campos de resposta
    private String numeroPedido;
    private BigDecimal valorTotal;
    private String status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String clienteNome;
    private String restauranteNome;
}