package com.deliverytech.delivery.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Produto;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {
    public Produto findByNome(String nome);
    public boolean existsByNome(String nome);
}