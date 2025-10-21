package com.deliverytech.delivery.service;

import java.util.List;

import com.deliverytech.delivery.dto.ClienteDTO;

public interface IClienteService {

    List<ClienteDTO> findAll();
    ClienteDTO findById(Long id);
    ClienteDTO create(ClienteDTO clienteDTO);
    ClienteDTO update(Long id, ClienteDTO clienteDTO);
    void delete(Long id);
    
    // Métodos adicionais específicos para Cliente
    List<ClienteDTO> findByAtivoTrue();
    ClienteDTO findByEmail(String email);
    boolean existsByEmail(String email);
}