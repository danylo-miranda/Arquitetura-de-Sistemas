package com.deliverytech.delivery.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;

import lombok.Data;
 
@Entity
@Data
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
    @Column(length = 10)
    private String telefone;
    private BigDecimal taxaEntrega;
    private boolean ativo;
    @Column(length = 14, nullable = false)
    private String CNPJ;
    private LocalDateTime dataCadastro = LocalDateTime.now();
 
    @OneToMany(mappedBy = "restaurante")
    private List<Produto> produtos;
 
    @OneToMany(mappedBy = "restaurante")
    private List<Pedido> pedidos;
}