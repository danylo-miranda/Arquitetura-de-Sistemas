package com.deliverytech.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.math.BigDecimal;

import lombok.Data;
 
@Data
public class RestaurantDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 25, message = "Nome ter no máximo 25 caracteres")
    private String nome;
    
    @NotBlank(message = "Categoria é obrigatório")
    @Size(max = 50, message = "Categoria ter no máximo 50 caracteres")
    private String categoria;
    
    @NotBlank(message = "Endereco é obrigatório")
    @Size(max = 100, message = "Endereco ter no máximo 100 caracteres")
    private String endereco;
    
    @NotBlank(message = "CEP é obrigatório")
    @Size(max = 8, message = "CEP ter no máximo 8 caracteres")
    private String CEP;
    
    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 10, message = "Telefone ter no máximo 10 caracteres")
    private String telefone;
    private BigDecimal taxaEntrega;

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(max = 14, message = "CNPJ ter no máximo 14 caracteres")
    private String CNPJ;
    private boolean ativo;    
}