package com.deliverytech.delivery.service;

import java.util.List;
import com.deliverytech.delivery.dto.RestaurantDTO;

public interface IRestaurantService {
    List<RestaurantDTO> getAllRestaurants();
    Long createRestaurant(RestaurantDTO restaurantDTO);
}