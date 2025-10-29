package com.deliverytech.delivery.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CalculoPedidoDetalhadoResponseDTO {
    private BigDecimal valorSubtotal;
    private BigDecimal taxaEntrega;
    private BigDecimal valorTotal;
    private Integer quantidadeItens;
    private String restauranteNome;
    private String restauranteCategoria;
    private List<ItemCalculoDetalhadoDTO> itensDetalhados;
    private String mensagem;

    public CalculoPedidoDetalhadoResponseDTO() {}

    public CalculoPedidoDetalhadoResponseDTO(BigDecimal valorSubtotal, BigDecimal taxaEntrega, 
                                            BigDecimal valorTotal, Integer quantidadeItens, 
                                            String restauranteNome, String restauranteCategoria,
                                            List<ItemCalculoDetalhadoDTO> itensDetalhados) {
        this.valorSubtotal = valorSubtotal;
        this.taxaEntrega = taxaEntrega;
        this.valorTotal = valorTotal;
        this.quantidadeItens = quantidadeItens;
        this.restauranteNome = restauranteNome;
        this.restauranteCategoria = restauranteCategoria;
        this.itensDetalhados = itensDetalhados;
    }
}