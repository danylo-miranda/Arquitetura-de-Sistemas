package com.deliverytech.delivery.service;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.repository.IClienteRepository;
import com.deliverytech.delivery.repository.IRestauranteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidadorPedidoService {

    private final IClienteRepository clienteRepository;
    private final IRestauranteRepository restauranteRepository;

    /**
     * Valida se o cliente existe e está ativo
     */
    public Cliente validarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
        return cliente;
    }

    /**
     * Valida se o restaurante existe e está ativo
     */
    public Restaurante validarRestaurante(Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com ID: " + restauranteId));

        if (!restaurante.isAtivo()) {
            throw new RuntimeException("Restaurante não está ativo");
        }

        return restaurante;
    }

    /**
     * Valida a transição de status do pedido
     */
    public void validarTransicaoStatus(StatusPedido statusAtual, StatusPedido novoStatus) {
        if (statusAtual == StatusPedido.ENTREGUE && novoStatus != StatusPedido.ENTREGUE) {
            throw new RuntimeException("Pedido já entregue não pode ter status alterado");
        }
        
        if (statusAtual == StatusPedido.CANCELADO && novoStatus != StatusPedido.CANCELADO) {
            throw new RuntimeException("Pedido cancelado não pode ter status alterado");
        }

        // Validações adicionais de negócio
        if (statusAtual == StatusPedido.CANCELADO) {
            throw new RuntimeException("Pedido cancelado não pode ser modificado");
        }

        if (statusAtual == StatusPedido.ENTREGUE) {
            throw new RuntimeException("Pedido entregue não pode ser modificado");
        }
    }

    /**
     * Valida se o pedido pode ser cancelado
     */
    public void validarCancelamento(StatusPedido statusAtual) {
        if (statusAtual != StatusPedido.PENDENTE && statusAtual != StatusPedido.CONFIRMADO) {
            throw new RuntimeException("Não é possível cancelar o pedido no status: " + statusAtual);
        }
    }

    /**
     * Valida se o pedido pode ser excluído
     */
    public void validarExclusao(StatusPedido statusAtual) {
        if (statusAtual != StatusPedido.CANCELADO) {
            throw new RuntimeException("Só é possível excluir pedidos cancelados. Status atual: " + statusAtual);
        }
    }
}