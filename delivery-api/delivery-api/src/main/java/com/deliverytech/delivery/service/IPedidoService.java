package com.deliverytech.delivery.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

import com.deliverytech.delivery.dto.CalculoPedidoRequestDTO;
import com.deliverytech.delivery.dto.CalculoPedidoResponseDTO;
import com.deliverytech.delivery.dto.PedidoDTO;
import com.deliverytech.delivery.entity.StatusPedido;

/**
 * Interface para o serviço de gerenciamento de pedidos
 */
public interface IPedidoService {
    
    /**
     * Cria um novo pedido
     * @param dto Dados do pedido a ser criado
     * @return PedidoDTO com os dados do pedido criado
     */
    PedidoDTO criarPedido(PedidoDTO dto);
    
    /**
     * Busca um pedido pelo ID
     * @param id ID do pedido
     * @return PedidoDTO com os dados do pedido
     */
    PedidoDTO buscarPedidoPorId(Long id);
    
    /**
     * Lista pedidos com filtros opcionais
     * @param status Status do pedido (opcional)
     * @param dataInicio Data inicial (opcional)
     * @param dataFim Data final (opcional)
     * @param pageable Parâmetros de paginação
     * @return Lista de PedidoDTO
     */
    List<PedidoDTO> listarPedidos(StatusPedido status, LocalDate dataInicio, LocalDate dataFim, Pageable pageable);
    
    /**
     * Atualiza o status de um pedido
     * @param id ID do pedido
     * @param status Novo status do pedido
     * @return PedidoDTO com o status atualizado
     */
    PedidoDTO atualizarStatusPedido(Long id, StatusPedido status);
    
    /**
     * Cancela um pedido
     * @param id ID do pedido a ser cancelado
     */
    void cancelarPedido(Long id);
    
    /**
     * Busca pedidos por cliente
     * @param clienteId ID do cliente
     * @return Lista de pedidos do cliente
     */
    List<PedidoDTO> buscarPedidosPorCliente(Long clienteId);
    
    /**
     * Busca pedidos por restaurante
     * @param restauranteId ID do restaurante
     * @param status Status do pedido (opcional)
     * @return Lista de pedidos do restaurante
     */
    List<PedidoDTO> buscarPedidosPorRestaurante(Long restauranteId, StatusPedido status);
    
    /**
     * Calcula o total de um pedido sem salvá-lo
     * @param dto Dados para cálculo do pedido
     * @return CalculoPedidoResponseDTO com o total calculado
     */
    CalculoPedidoResponseDTO calcularTotalPedido(CalculoPedidoRequestDTO dto);
}