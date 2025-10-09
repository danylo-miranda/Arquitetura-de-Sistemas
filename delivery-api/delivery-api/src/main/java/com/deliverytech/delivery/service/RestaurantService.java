package com.deliverytech.delivery.service;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.dto.RestaurantDTO;
import com.deliverytech.delivery.entity.Restaurant;
import com.deliverytech.delivery.repository.IRestaurantRepository;


@Service
public class RestaurantService implements IRestaurantService {
 
    @Autowired
    private IRestaurantRepository repository;
 
    @Override
    public RestaurantDTO create(RestaurantDTO restaurantDTO) {
       ModelMapper mapper = new ModelMapper();
 
       Restaurant entity = mapper.map(restaurantDTO, Restaurant.class);
       Restaurant restaurant = repository.save(entity);
       RestaurantDTO dto = mapper.map(restaurant, RestaurantDTO.class);
 
       return dto;
    }
 
    @Override
    public List<RestaurantDTO> findAll() {
        ModelMapper mapper = new ModelMapper();  
        List<Restaurant> restaurantes = repository.findAll();
 
        List<RestaurantDTO> restaurantDtoList = Arrays.asList(mapper.map(restaurantes, RestaurantDTO[].class));
 
        return restaurantDtoList;
    }
    
    @Override
    public RestaurantDTO findById(Long id) {
        ModelMapper mapper = new ModelMapper();
        Restaurant restaurant = repository.findById(id).orElse(null);
        if (restaurant == null) {
            return null;
        }
        return mapper.map(restaurant, RestaurantDTO.class);
    }
    
    @Override
    public RestaurantDTO update(Long id, RestaurantDTO restaurantDTO) {
        ModelMapper mapper = new ModelMapper();
        Restaurant restaurant = repository.findById(id).orElse(null);
        if (restaurant == null) {
            return null;
        }
        
        restaurant.setNome(restaurantDTO.getNome());
        restaurant.setDescricao(restaurantDTO.getDescricao());
        
        Restaurant updatedRestaurant = repository.save(restaurant);
        return mapper.map(updatedRestaurant, RestaurantDTO.class);
    }
    
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}