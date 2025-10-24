package com.deliverytech.delivery.service;

import java.util.List;

import com.deliverytech.delivery.dto.PedidoDTO;
import com.deliverytech.delivery.dto.PedidoResponseDTO;
import com.deliverytech.delivery.entity.StatusPedido;

public interface IPedidoService {
    
    PedidoResponseDTO create(PedidoDTO pedidoDTO);
    
    List<PedidoResponseDTO> findAll();
    
    PedidoResponseDTO findById(Long id);
    
    PedidoResponseDTO findByNumeroPedido(String numeroPedido);
    
    PedidoResponseDTO updateStatus(Long id, StatusPedido status);
    
    PedidoResponseDTO cancelarPedido(Long id);
    
    List<PedidoResponseDTO> findByClienteId(Long clienteId);
    
    List<PedidoResponseDTO> findByRestauranteId(Long restauranteId);
    
    List<PedidoResponseDTO> findByStatus(StatusPedido status);
    
    void delete(Long id);
}