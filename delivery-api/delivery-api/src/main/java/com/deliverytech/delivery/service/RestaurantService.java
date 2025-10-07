package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.RestaurantDTO;
import com.deliverytech.delivery.entity.Restaurant;
import com.deliverytech.delivery.repository.IRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("restaurantService") // ← NOME DO BEAN EXPLÍCITO
public class RestaurantService implements IRestaurantService {
    
    @Autowired
    private IRestaurantRepository restaurantRepository;
    
    @Override
    public List<RestaurantDTO> findAll() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public RestaurantDTO findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
        return convertToDTO(restaurant);
    }
    
    @Override
    public RestaurantDTO create(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = convertToEntity(restaurantDTO);
        Restaurant saved = restaurantRepository.save(restaurant);
        return convertToDTO(saved);
    }
    
    @Override
    public RestaurantDTO update(Long id, RestaurantDTO restaurantDTO) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
        
        existing.setName(restaurantDTO.getName());
        existing.setDescription(restaurantDTO.getDescription());
        existing.setCuisine(restaurantDTO.getCuisine());
        
        Restaurant updated = restaurantRepository.save(existing);
        return convertToDTO(updated);
    }
    
    @Override
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }
    
    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setCuisine(restaurant.getCuisine());
        return dto;
    }
    
    private Restaurant convertToEntity(RestaurantDTO dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setCuisine(dto.getCuisine());
        return restaurant;
    }
}