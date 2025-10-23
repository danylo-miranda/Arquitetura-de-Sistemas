package com.deliverytech.delivery.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Restaurante;
import java.util.List;
import java.util.Optional;

@Repository
public interface IRestauranteRepository extends JpaRepository<Restaurante, Long> {
    
    Optional<Restaurante> findByCNPJ(String cnpj);
    
    boolean existsByCNPJ(String cnpj);
    
    List<Restaurante> findByAtivoTrue();
    
    List<Restaurante> findByCategoria(String categoria);
}