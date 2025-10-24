package com.deliverytech.delivery.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PedidoResponseDTO {
    private Long id;
    private String numeroPedido;
    private Long clienteId;
    private String clienteNome;
    private Long restauranteId;
    private String restauranteNome;
    private String enderecoEntrega;
    private BigDecimal valorTotal;
    private String status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<ItemPedidoResponseDTO> itens;

    // Construtor vazio
    public PedidoResponseDTO() {}

    // Construtor com todos os campos
    public PedidoResponseDTO(Long id, String numeroPedido, Long clienteId, String clienteNome, 
                           Long restauranteId, String restauranteNome, String enderecoEntrega, 
                           BigDecimal valorTotal, String status, LocalDateTime dataCriacao, 
                           LocalDateTime dataAtualizacao, List<ItemPedidoResponseDTO> itens) {
        this.id = id;
        this.numeroPedido = numeroPedido;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.restauranteId = restauranteId;
        this.restauranteNome = restauranteNome;
        this.enderecoEntrega = enderecoEntrega;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.itens = itens;
    }

}
