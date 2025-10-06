package com.deliverytech.delivery.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody RestaurantDTO restaurantDTO) {
        Long ok = restaurantService.createRestaurant(restaurantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ok);
    }
}