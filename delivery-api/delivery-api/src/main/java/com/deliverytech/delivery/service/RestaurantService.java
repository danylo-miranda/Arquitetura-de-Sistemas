package com.deliverytech.delivery.service;

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
 
    public RestaurantService(IRestaurantRepository repository)
    {
        this.repository = repository;
    }
 
    public RestaurantService() {
        super();
    }

    @Override
    public RestaurantDTO create(RestaurantDTO restaurantDTO) {
        return createRestaurant(restaurantDTO);
    }

    @Override
    public List<RestaurantDTO> findAll() {
        return getAllRestaurants();
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
        Restaurant restaurant = repository.findById(id).orElse(null);
        if (restaurant == null) {
            return null;
        }
        restaurant.setName(restaurantDTO.getName());
        restaurant.setDescription(restaurantDTO.getDescription());
        Restaurant updatedRestaurant = repository.save(restaurant);
        ModelMapper mapper = new ModelMapper();
        return mapper.map(updatedRestaurant, RestaurantDTO.class);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
       ModelMapper mapper = new ModelMapper();
 
       Restaurant entity = mapper.map(restaurantDTO, Restaurant.class);
       Restaurant restaurant = repository.save(entity);
       RestaurantDTO dto = mapper.map(restaurant, RestaurantDTO.class);
 
       return dto;
    }
 
public List<RestaurantDTO> getAllRestaurants() {
    List<Restaurant> restaurantes = repository.findAll();

    List<RestaurantDTO> restaurantDtoList = restaurantes.stream()
        .map(this::ConvertEntityToDto)
        .toList();

    return restaurantDtoList;
}
   
 
    private RestaurantDTO ConvertEntityToDto(Restaurant entity) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setName(entity.getName());
        dto.setDescription((entity.getDescription()));
        return dto;
    }
 
}