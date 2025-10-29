package com.deliverytech.delivery.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private boolean disponivel;
    private Long idRestaurante;
    private String nomeRestaurante;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}