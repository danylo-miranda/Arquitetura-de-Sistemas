package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.deliverytech.delivery.dto.RestaurantDTO;
import com.deliverytech.delivery.service.IRestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/restaurantes")
@Tag(name = "Restaurantes", description = "Operações para gerenciamento de restaurantes")
public class RestauranteController {
    
    @Autowired
    @Qualifier("restaurantService") // ← REFERÊNCIA EXPLÍCITA AO BEAN
    private IRestaurantService restaurantService;
    
    @Operation(summary = "Listar todos os restaurantes")
    @GetMapping
    public List<RestaurantDTO> listarRestaurantes() {
        return restaurantService.findAll();
    }
    
    @Operation(summary = "Buscar restaurante por ID")
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> buscarRestaurante(@PathVariable Long id) {
        try {
            RestaurantDTO restaurante = restaurantService.findById(id);
            return ResponseEntity.ok(restaurante);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Criar novo restaurante")
    @PostMapping
    public RestaurantDTO criarRestaurante(@RequestBody RestaurantDTO restauranteDTO) {
        return restaurantService.create(restauranteDTO);
    }
    
    @Operation(summary = "Atualizar restaurante")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> atualizarRestaurante(@PathVariable Long id, @RequestBody RestaurantDTO restauranteDTO) {
        try {
            RestaurantDTO atualizado = restaurantService.update(id, restauranteDTO);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Deletar restaurante")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRestaurante(@PathVariable Long id) {
        try {
            restaurantService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}