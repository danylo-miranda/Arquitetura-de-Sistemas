package com.deliverytech.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Restaurant;

@Repository
public interface  IRestaurantRepository extends JpaRepository<Restaurant, Long>{
    List<Restaurant> findByCuisine(String cuisine);
}
