package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.dto.ItemPedidoDTO;
import com.deliverytech.delivery.dto.ItemPedidoResponseDTO;
import com.deliverytech.delivery.dto.PedidoDTO;
import com.deliverytech.delivery.dto.PedidoResponseDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.entity.ItemPedido;
import com.deliverytech.delivery.entity.Pedido;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.repository.IClienteRepository;
import com.deliverytech.delivery.repository.IItemPedidoRepository;
import com.deliverytech.delivery.repository.IPedidoRepository;
import com.deliverytech.delivery.repository.IProdutoRepository;
import com.deliverytech.delivery.repository.IRestauranteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService implements IPedidoService {

    private final IPedidoRepository pedidoRepository;
    private final IClienteRepository clienteRepository;
    private final IRestauranteRepository restauranteRepository;
    private final IProdutoRepository produtoRepository;
    private final IItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public PedidoResponseDTO create(PedidoDTO pedidoDTO) {
        // Validar cliente
        Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + pedidoDTO.getClienteId()));

        // Validar restaurante
        Restaurante restaurante = restauranteRepository.findById(pedidoDTO.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com ID: " + pedidoDTO.getRestauranteId()));

        // Validar se restaurante está ativo
        if (!restaurante.isAtivo()) {
            throw new RuntimeException("Restaurante não está ativo");
        }

        // Criar pedido
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(gerarNumeroPedido());
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega(pedidoDTO.getEnderecoEntrega());
        pedido.setStatus(StatusPedido.PENDENTE);

        // Adicionar itens ao pedido
        BigDecimal valorTotalItens = BigDecimal.ZERO;
        
        for (ItemPedidoDTO itemDTO : pedidoDTO.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDTO.getProdutoId()));

            // Verificar se o produto pertence ao restaurante
            if (!produto.getRestaurante().getId().equals(restaurante.getId())) {
                throw new RuntimeException("Produto não pertence ao restaurante selecionado");
            }

            // Verificar se produto está disponível
            if (!produto.isDisponivel()) {
                throw new RuntimeException("Produto não está disponível: " + produto.getNome());
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDTO.getQuantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());
            itemPedido.calcularSubtotal();

            pedido.adicionarItem(itemPedido);
            valorTotalItens = valorTotalItens.add(itemPedido.getSubtotal());
        }

        // Calcular valor total (itens + taxa de entrega)
        BigDecimal valorTotal = valorTotalItens.add(restaurante.getTaxaEntrega() != null ? restaurante.getTaxaEntrega() : BigDecimal.ZERO);
        pedido.setValorTotal(valorTotal);

        // Salvar pedido
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return toResponseDTO(pedidoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findAll() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        return toResponseDTO(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO findByNumeroPedido(String numeroPedido) {
        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + numeroPedido));
        return toResponseDTO(pedido);
    }

    @Override
    @Transactional
    public PedidoResponseDTO updateStatus(Long id, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        // Validar transição de status
        validarTransicaoStatus(pedido.getStatus(), status);

        pedido.setStatus(status);
        pedido.setDataAtualizacao(LocalDateTime.now());

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return toResponseDTO(pedidoAtualizado);
    }

    @Override
    @Transactional
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        // Validar se pode cancelar
        if (pedido.getStatus() != StatusPedido.PENDENTE && pedido.getStatus() != StatusPedido.CONFIRMADO) {
            throw new RuntimeException("Não é possível cancelar o pedido no status: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setDataAtualizacao(LocalDateTime.now());
        
        Pedido pedidoCancelado = pedidoRepository.save(pedido);
        return toResponseDTO(pedidoCancelado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findByClienteId(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RuntimeException("Cliente não encontrado com ID: " + clienteId);
        }

        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findByRestauranteId(Long restauranteId) {
        if (!restauranteRepository.existsById(restauranteId)) {
            throw new RuntimeException("Restaurante não encontrado com ID: " + restauranteId);
        }

        return pedidoRepository.findByRestauranteId(restauranteId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findByStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        // Só permite deletar pedidos cancelados
        if (pedido.getStatus() != StatusPedido.CANCELADO) {
            throw new RuntimeException("Só é possível excluir pedidos cancelados");
        }

        pedidoRepository.deleteById(id);
    }

    // Métodos auxiliares privados
    private void validarTransicaoStatus(StatusPedido statusAtual, StatusPedido novoStatus) {
        if (statusAtual == StatusPedido.ENTREGUE && novoStatus != StatusPedido.ENTREGUE) {
            throw new RuntimeException("Pedido já entregue não pode ter status alterado");
        }
        
        if (statusAtual == StatusPedido.CANCELADO && novoStatus != StatusPedido.CANCELADO) {
            throw new RuntimeException("Pedido cancelado não pode ter status alterado");
        }
    }

    private String gerarNumeroPedido() {
        String numero = "PED" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Garantir que o número é único
        while (pedidoRepository.existsByNumeroPedido(numero)) {
            numero = "PED" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        
        return numero;
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setId(pedido.getId());
        response.setNumeroPedido(pedido.getNumeroPedido());
        
        if (pedido.getCliente() != null) {
            response.setClienteId(pedido.getCliente().getId());
            response.setClienteNome(pedido.getCliente().getNome());
        }
        
        if (pedido.getRestaurante() != null) {
            response.setRestauranteId(pedido.getRestaurante().getId());
            response.setRestauranteNome(pedido.getRestaurante().getNome());
        }
        
        response.setEnderecoEntrega(pedido.getEnderecoEntrega());
        response.setValorTotal(pedido.getValorTotal());
        response.setStatus(pedido.getStatus().name());
        response.setDataCriacao(pedido.getDataCriacao());
        response.setDataAtualizacao(pedido.getDataAtualizacao());

        // Converter itens
        if (pedido.getItens() != null) {
            List<ItemPedidoResponseDTO> itensResponse = pedido.getItens().stream()
                    .map(this::toItemResponseDTO)
                    .collect(Collectors.toList());
            response.setItens(itensResponse);
        }
        
        return response;
    }

    private ItemPedidoResponseDTO toItemResponseDTO(ItemPedido item) {
        ItemPedidoResponseDTO itemResponse = new ItemPedidoResponseDTO();
        itemResponse.setProdutoId(item.getProduto().getId());
        itemResponse.setProdutoNome(item.getProduto().getNome());
        itemResponse.setProdutoDescricao(item.getProduto().getDescricao());
        itemResponse.setQuantidade(item.getQuantidade());
        itemResponse.setPrecoUnitario(item.getPrecoUnitario());
        itemResponse.setSubtotal(item.getSubtotal());
        return itemResponse;
    }
}