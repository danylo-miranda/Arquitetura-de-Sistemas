package com.deliverytech.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Produto;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {
    
    /**
     * Verifica se existe produto com o mesmo nome
     */
    boolean existsByNome(String nome);
    
    /**
     * Busca produtos por categoria e que estão disponíveis
     */
    List<Produto> findByCategoriaAndDisponivelTrue(String categoria);
    
    /**
     * Busca produtos por nome (busca parcial) e que estão disponíveis
     */
    List<Produto> findByNomeContainingIgnoreCaseAndDisponivelTrue(String nome);
    
    /**
     * Busca produtos por restaurante
     */
    List<Produto> findByRestauranteId(Long restauranteId);
    
    /**
     * Busca produtos por restaurante e que estão disponíveis
     */
    List<Produto> findByRestauranteIdAndDisponivelTrue(Long restauranteId);
    
    /**
     * Busca produto por nome e restaurante (para validação de unicidade)
     */
    Optional<Produto> findByNomeAndRestauranteId(String nome, Long restauranteId);
    
    /**
     * Verifica se existe produto com mesmo nome no restaurante
     */
    boolean existsByNomeAndRestauranteId(String nome, Long restauranteId);
}