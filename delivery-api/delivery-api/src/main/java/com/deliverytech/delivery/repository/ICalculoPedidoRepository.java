package com.deliverytech.delivery.repository;

import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICalculoPedidoRepository extends JpaRepository<Produto, Long> {
    
    /**
     * Busca produtos por IDs com verificação de disponibilidade
     */
    @Query("SELECT p FROM Produto p WHERE p.id IN :produtoIds AND p.disponivel = true")
    List<Produto> findProdutosDisponiveisByIds(@Param("produtoIds") List<Long> produtoIds);
    
    /**
     * Busca restaurante com taxa de entrega
     */
    @Query("SELECT r FROM Restaurante r WHERE r.id = :restauranteId AND r.ativo = true")
    Optional<Restaurante> findRestauranteAtivoComTaxa(@Param("restauranteId") Long restauranteId);
    
    /**
     * Verifica se produtos pertencem ao restaurante
     */
    @Query("SELECT COUNT(p) FROM Produto p WHERE p.id IN :produtoIds AND p.restaurante.id = :restauranteId")
    Long countProdutosDoRestaurante(@Param("produtoIds") List<Long> produtoIds, @Param("restauranteId") Long restauranteId);
    
    /**
     * Busca preço médio dos produtos de um restaurante
     */
    @Query("SELECT AVG(p.preco) FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true")
    BigDecimal findPrecoMedioPorRestaurante(@Param("restauranteId") Long restauranteId);
    
    /**
     * Busca produtos populares do restaurante
     */
    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true ORDER BY p.id DESC LIMIT 5")
    List<Produto> findProdutosPopularesPorRestaurante(@Param("restauranteId") Long restauranteId);
}