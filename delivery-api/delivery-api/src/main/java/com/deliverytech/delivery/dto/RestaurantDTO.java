package com.deliverytech.delivery.dto;
 
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.Data;
 
@Data
public class RestaurantDTO {
    private Long id;
    private String nome;    
    private String descricao;
    private String cozinha;
    private BigDecimal estrelas;
    private OffsetDateTime horario;
    private String telefone;
    private String endereco;
    private String CNPJ;
}