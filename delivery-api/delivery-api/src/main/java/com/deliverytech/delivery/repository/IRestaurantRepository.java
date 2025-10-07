package com.deliverytech.delivery.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Restaurant;

@Repository
public interface IRestaurantRepository extends JpaRepository<Restaurant, Long>{
    public Restaurant findByName(String name); 
    public boolean existsByName(String name);
}