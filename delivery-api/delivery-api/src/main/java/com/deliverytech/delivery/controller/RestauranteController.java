package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.RestaurantDTO;
import com.deliverytech.delivery.service.IRestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestauranteController {
    
    @Autowired
    private IRestaurantService restaurantService;
    
    // Construtor com log
    public RestauranteController() {
        System.out.println("RestauranteController inicializado!");
    }
 
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> list() {
        List<RestaurantDTO> restaurants = restaurantService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(restaurants);
    }
 
    @PostMapping
    public ResponseEntity<RestaurantDTO> create(@Valid @RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO restaurant = restaurantService.create(restaurantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
    }
}