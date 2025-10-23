package com.deliverytech.delivery.dto;
 
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;
 
@Data
public class ProdutoDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 25, message = "Nome ter no máximo 25 caracteres")
    private String nome;
    @Size(max = 200, message = "Nome ter no máximo 200 caracteres")
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private boolean disponivel;
    @NotNull(message = "idRestaurante é obrigatório")
    private Long idRestaurante;
}
 
