package com.deliverytech.delivery.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;    
    private String descricao;
    private String cozinha;
    private BigDecimal estrelas;
    private OffsetDateTime horario;
    private String telefone;
    private String endereco;
    private String CNPJ;
    
    @OneToMany(mappedBy="restaurant")
    private List<Produto> menu;
}