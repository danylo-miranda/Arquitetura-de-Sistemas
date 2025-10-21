package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.ClienteDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/api/restaurants")
public class ClienteController {
    
    private List<ClienteDTO> clientes; // injetar um service aqui ***********

    // POST /v1/api/restaurants/clientes
    @PostMapping("/clientes")
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO clienteDTO) {
        // Lógica para criar cliente
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDTO);
    }

    // GET /v1/api/restaurants/clientes
    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteDTO>> list() {
        // Lógica para listar clientes
        return ResponseEntity.status(HttpStatus.OK).body(clientes);
    }

    // GET /v1/api/restaurants/clientes/{id}
    @GetMapping("/clientes/{id}")
    public ResponseEntity<ClienteDTO> get(@PathVariable Long id) {
        // Lógica para buscar cliente por ID
        ClienteDTO cliente = null; // Buscar do service
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizarCliente(
        @PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
            ClienteDTO cliente = null;//clienteService.atualizarCliente(id, dto);
            return ResponseEntity.ok(cliente);
    }

    // DELETE /v1/api/restaurants/clientes/{id}
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Lógica para deletar cliente
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    
}
