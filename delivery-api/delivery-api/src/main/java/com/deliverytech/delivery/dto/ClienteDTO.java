package com.deliverytech.delivery.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ClienteDTO {
    private String nome;
    private String email;
    private String telefone; 
    private String endereco; 
    private Boolean ativo;
}
