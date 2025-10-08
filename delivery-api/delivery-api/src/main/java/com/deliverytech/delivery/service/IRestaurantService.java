package com.deliverytech.delivery.service;

import java.util.List;

import com.deliverytech.delivery.dto.RestaurantDTO;


public interface IRestaurantService {

    List<RestaurantDTO> findAll();
    RestaurantDTO findById(Long id);
    RestaurantDTO create(RestaurantDTO restaurantDTO);
    RestaurantDTO update(Long id, RestaurantDTO restaurantDTO);

    void delete(Long id);
}

