package com.deliverytech.delivery.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "restaurantes")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 25, nullable = false)
    private String nome;
    
    @Column(length = 50)
    private String categoria;
    
    @Column(length = 100)
    private String endereco;
    
    @Column(length = 8, nullable = false)
    private String CEP;
    
    @Column(length = 15) // ⚠️ AUMENTADO para 15 caracteres (DDD + número)
    private String telefone;
    
    private BigDecimal taxaEntrega;
    
    private boolean ativo = true;
    
    @Column(length = 14, nullable = false, unique = true)
    private String CNPJ;
    
    private LocalDateTime dataCadastro = LocalDateTime.now();
}