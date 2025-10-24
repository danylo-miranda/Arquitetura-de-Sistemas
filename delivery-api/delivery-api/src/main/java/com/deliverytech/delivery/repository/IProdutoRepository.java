package com.deliverytech.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Produto;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {
    
    // ✅ Método findById já está disponível pelo JpaRepository
    
    List<Produto> findByRestauranteId(Long restauranteId);
    
    List<Produto> findByCategoria(String categoria);
    
    List<Produto> findByDisponivelTrue();
    
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);
    
    Optional<Produto> findByNomeAndRestauranteId(String nome, Long restauranteId);
    
    boolean existsByNomeAndRestauranteId(String nome, Long restauranteId);
}