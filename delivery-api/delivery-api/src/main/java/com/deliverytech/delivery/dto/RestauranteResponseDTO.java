package com.deliverytech.delivery.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RestauranteResponseDTO {
    private Long id;
    private String nome;
    private String categoria;
    private String endereco;
    private String CEP;
    private String telefone;
    private BigDecimal taxaEntrega;
    private String CNPJ;
    private boolean ativo;    
    private LocalDateTime dataCadastro;
    
}
