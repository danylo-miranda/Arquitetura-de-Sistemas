package com.deliverytech.delivery.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Restaurant;

@Repository
public interface IRestaurantRepository extends JpaRepository<Restaurant, Long>{
    public Restaurant findByNome(String nome); 
    public boolean existsByNome(String nome);
}