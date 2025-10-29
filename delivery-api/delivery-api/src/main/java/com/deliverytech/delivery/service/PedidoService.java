package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.entity.*;
import com.deliverytech.delivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService implements IPedidoService {

    private final IPedidoRepository pedidoRepository;
    private final IClienteRepository clienteRepository;
    private final IRestauranteRepository restauranteRepository;
    private final IProdutoRepository produtoRepository;

    @Override
    @Transactional
    public PedidoDTO criarPedido(PedidoDTO dto) {
        // Validar cliente
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + dto.getClienteId()));

        // Validar restaurante
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com ID: " + dto.getRestauranteId()));

        // Validar se restaurante está ativo
        if (!restaurante.isAtivo()) {
            throw new RuntimeException("Restaurante não está ativo");
        }

        // Criar pedido
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(gerarNumeroPedido());
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setStatus(StatusPedido.PENDENTE);

        // Adicionar itens ao pedido
        BigDecimal valorTotalItens = BigDecimal.ZERO;
        
        for (ItemPedidoDTO itemDTO : dto.getItens()) {
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

        return toPedidoDTO(pedidoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoDTO buscarPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        return toPedidoDTO(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidos(StatusPedido status, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        List<Pedido> pedidos;
        
        if (status != null) {
            pedidos = pedidoRepository.findByStatus(status);
        } else {
            pedidos = pedidoRepository.findAll();
        }
        
        // Aplicar filtro de data se fornecido
        if (dataInicio != null && dataFim != null) {
            pedidos = pedidos.stream()
                    .filter(pedido -> {
                        LocalDateTime dataPedido = pedido.getDataCriacao();
                        return !dataPedido.toLocalDate().isBefore(dataInicio) && 
                               !dataPedido.toLocalDate().isAfter(dataFim);
                    })
                    .collect(Collectors.toList());
        }

        return pedidos.stream()
                .map(this::toPedidoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PedidoDTO atualizarStatusPedido(Long id, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        // Validar transição de status
        validarTransicaoStatus(pedido.getStatus(), status);

        pedido.setStatus(status);
        pedido.setDataAtualizacao(LocalDateTime.now());

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return toPedidoDTO(pedidoAtualizado);
    }

    @Override
    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));

        // Validar se pode cancelar
        if (pedido.getStatus() != StatusPedido.PENDENTE && pedido.getStatus() != StatusPedido.CONFIRMADO) {
            throw new RuntimeException("Não é possível cancelar o pedido no status: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedido.setDataAtualizacao(LocalDateTime.now());
        pedidoRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> buscarPedidosPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RuntimeException("Cliente não encontrado com ID: " + clienteId);
        }

        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::toPedidoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> buscarPedidosPorRestaurante(Long restauranteId, StatusPedido status) {
        if (!restauranteRepository.existsById(restauranteId)) {
            throw new RuntimeException("Restaurante não encontrado com ID: " + restauranteId);
        }

        List<Pedido> pedidos;
        if (status != null) {
            pedidos = pedidoRepository.findByRestauranteIdAndStatus(restauranteId, status);
        } else {
            pedidos = pedidoRepository.findByRestauranteId(restauranteId);
        }

        return pedidos.stream()
                .map(this::toPedidoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CalculoPedidoResponseDTO calcularTotalPedido(CalculoPedidoRequestDTO dto) {
        // Validar restaurante
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado com ID: " + dto.getRestauranteId()));

        BigDecimal valorTotalItens = BigDecimal.ZERO;
        int quantidadeItens = 0;

        // Calcular valor dos itens
        for (CalculoPedidoRequestDTO.ItemCalculoDTO item : dto.getItens()) {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + item.getProdutoId()));

            // Verificar se produto está disponível
            if (!produto.isDisponivel()) {
                throw new RuntimeException("Produto não está disponível: " + produto.getNome());
            }

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            valorTotalItens = valorTotalItens.add(subtotal);
            quantidadeItens += item.getQuantidade();
        }

        // Calcular valor total (itens + taxa de entrega)
        BigDecimal taxaEntrega = restaurante.getTaxaEntrega() != null ? restaurante.getTaxaEntrega() : BigDecimal.ZERO;
        BigDecimal valorTotal = valorTotalItens.add(taxaEntrega);

        return new CalculoPedidoResponseDTO(
                valorTotalItens,
                taxaEntrega,
                valorTotal,
                quantidadeItens,
                restaurante.getNome()
        );
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

    private PedidoDTO toPedidoDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        
        if (pedido.getCliente() != null) {
            dto.setClienteId(pedido.getCliente().getId());
            dto.setClienteNome(pedido.getCliente().getNome());
        }
        
        if (pedido.getRestaurante() != null) {
            dto.setRestauranteId(pedido.getRestaurante().getId());
            dto.setRestauranteNome(pedido.getRestaurante().getNome());
        }
        
        dto.setEnderecoEntrega(pedido.getEnderecoEntrega());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setStatus(pedido.getStatus().name());
        dto.setDataCriacao(pedido.getDataCriacao());
        dto.setDataAtualizacao(pedido.getDataAtualizacao());

        // Converter itens
        if (pedido.getItens() != null) {
            List<ItemPedidoDTO> itensDTO = pedido.getItens().stream()
                    .map(this::toItemPedidoDTO)
                    .collect(Collectors.toList());
            dto.setItens(itensDTO);
        }
        
        return dto;
    }

    private ItemPedidoDTO toItemPedidoDTO(ItemPedido item) {
        ItemPedidoDTO itemDTO = new ItemPedidoDTO();
        itemDTO.setProdutoId(item.getProduto().getId());
        itemDTO.setProdutoNome(item.getProduto().getNome());
        itemDTO.setQuantidade(item.getQuantidade());
        itemDTO.setPrecoUnitario(item.getPrecoUnitario());
        itemDTO.setSubtotal(item.getSubtotal());
        return itemDTO;
    }
}