package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery.dto.CalculoPedidoDetalhadoResponseDTO;
import com.deliverytech.delivery.dto.CalculoPedidoRequestDTO;
import com.deliverytech.delivery.dto.CalculoPedidoResponseDTO;
import com.deliverytech.delivery.dto.ItemCalculoDetalhadoDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.exception.ProdutoInvalidoException;
import com.deliverytech.delivery.exception.RestauranteNaoEncontradoException;
import com.deliverytech.delivery.repository.ICalculoPedidoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculoPedidoService {

    private final ICalculoPedidoRepository calculoPedidoRepository;

    /**
     * Cálculo simples do pedido
     */
    public CalculoPedidoResponseDTO calcularPedido(CalculoPedidoRequestDTO request) {
        log.info("Iniciando cálculo de pedido para restaurante ID: {}", request.getRestauranteId());

        Restaurante restaurante = validarRestaurante(request.getRestauranteId());
        Map<Long, Produto> produtosMap = validarEMapearProdutos(request, restaurante.getId());

        ResultadoCalculo resultado = calcularValores(request, produtosMap, restaurante);

        return new CalculoPedidoResponseDTO(
                resultado.valorSubtotal(),
                resultado.taxaEntrega(),
                resultado.valorTotal(),
                resultado.quantidadeItens(),
                restaurante.getNome()
        );
    }

    /**
     * Cálculo detalhado do pedido, com breakdown por item
     */
    public CalculoPedidoDetalhadoResponseDTO calcularPedidoDetalhado(CalculoPedidoRequestDTO request) {
        log.info("Iniciando cálculo detalhado de pedido para restaurante ID: {}", request.getRestauranteId());

        Restaurante restaurante = validarRestaurante(request.getRestauranteId());
        Map<Long, Produto> produtosMap = validarEMapearProdutos(request, restaurante.getId());

        ResultadoCalculoDetalhado resultado = calcularValoresDetalhados(request, produtosMap, restaurante);

        return new CalculoPedidoDetalhadoResponseDTO(
                resultado.valorSubtotal(),
                resultado.taxaEntrega(),
                resultado.valorTotal(),
                resultado.quantidadeItens(),
                restaurante.getNome(),
                restaurante.getCategoria(),
                resultado.itensDetalhados()
        );
    }

    /**
     * Simulação de cenários de pedido para um restaurante
     */
    public List<CalculoPedidoResponseDTO> simularMultiplosCenarios(Long restauranteId) {
        log.info("Simulando múltiplos cenários para restaurante ID: {}", restauranteId);

        validarRestaurante(restauranteId); // Just validate the restaurant exists
        List<Produto> produtosPopulares = calculoPedidoRepository.findProdutosPopularesPorRestaurante(restauranteId);

        if (produtosPopulares.isEmpty()) {
            throw new ProdutoInvalidoException("Nenhum produto popular encontrado para simulação.");
        }

        List<CalculoPedidoResponseDTO> simulacoes = new ArrayList<>();

        // Cenário 1: Pedido mínimo (1 produto)
        simulacoes.add(calcularPedido(criarRequest(restauranteId, produtosPopulares.get(0), 1)));

        // Cenário 2: Pedido médio (3 produtos)
        if (produtosPopulares.size() >= 3) {
            CalculoPedidoRequestDTO request2 = new CalculoPedidoRequestDTO();
            request2.setRestauranteId(restauranteId);
            request2.setItens(List.of(
                    criarItemCalculo(produtosPopulares.get(0).getId(), 2),
                    criarItemCalculo(produtosPopulares.get(1).getId(), 1),
                    criarItemCalculo(produtosPopulares.get(2).getId(), 1)
            ));
            simulacoes.add(calcularPedido(request2));
        }

        return simulacoes;
    }

    // =====================================
    // MÉTODOS AUXILIARES PRIVADOS
    // =====================================

    private Restaurante validarRestaurante(Long restauranteId) {
        return calculoPedidoRepository.findRestauranteAtivoComTaxa(restauranteId)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
    }

    private Map<Long, Produto> validarEMapearProdutos(CalculoPedidoRequestDTO request, Long restauranteId) {
        List<Long> produtoIds = request.getItens().stream()
                .map(CalculoPedidoRequestDTO.ItemCalculoDTO::getProdutoId)
                .toList();

        List<Produto> produtos = calculoPedidoRepository.findProdutosDisponiveisByIds(produtoIds);

        validarProdutosDoRestaurante(produtoIds, restauranteId, produtos);

        return produtos.stream().collect(Collectors.toUnmodifiableMap(Produto::getId, p -> p));
    }

    private void validarProdutosDoRestaurante(List<Long> produtoIds, Long restauranteId, List<Produto> produtosEncontrados) {
        if (produtosEncontrados.size() != produtoIds.size()) {
            List<Long> idsEncontrados = produtosEncontrados.stream()
                    .map(Produto::getId)
                    .toList();

            List<Long> idsNaoEncontrados = produtoIds.stream()
                    .filter(id -> !idsEncontrados.contains(id))
                    .toList();

            throw new ProdutoInvalidoException("Produtos não encontrados ou indisponíveis: " + idsNaoEncontrados);
        }

        long countProdutosRestaurante = calculoPedidoRepository.countProdutosDoRestaurante(produtoIds, restauranteId);

        if (countProdutosRestaurante != produtoIds.size()) {
            throw new ProdutoInvalidoException("Alguns produtos não pertencem ao restaurante informado.");
        }
    }

    private ResultadoCalculo calcularValores(CalculoPedidoRequestDTO request, Map<Long, Produto> produtoMap, Restaurante restaurante) {
        BigDecimal valorSubtotal = BigDecimal.ZERO;
        int quantidadeItens = 0;

        for (var item : request.getItens()) {
            Produto produto = Optional.ofNullable(produtoMap.get(item.getProdutoId()))
                    .orElseThrow(() -> new ProdutoInvalidoException("Produto inválido: " + item.getProdutoId()));

            BigDecimal subtotalItem = produto.getPreco()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()))
                    .setScale(2, RoundingMode.HALF_UP);

            valorSubtotal = valorSubtotal.add(subtotalItem);
            quantidadeItens += item.getQuantidade();
        }

        BigDecimal taxaEntrega = Optional.ofNullable(restaurante.getTaxaEntrega()).orElse(BigDecimal.ZERO);
        BigDecimal valorTotal = valorSubtotal.add(taxaEntrega).setScale(2, RoundingMode.HALF_UP);

        log.debug("Subtotal: {}, Taxa: {}, Total: {}", valorSubtotal, taxaEntrega, valorTotal);

        return new ResultadoCalculo(valorSubtotal, taxaEntrega, valorTotal, quantidadeItens);
    }

    private ResultadoCalculoDetalhado calcularValoresDetalhados(
            CalculoPedidoRequestDTO request,
            Map<Long, Produto> produtoMap,
            Restaurante restaurante) {

        BigDecimal valorSubtotal = BigDecimal.ZERO;
        int quantidadeItens = 0;
        List<ItemCalculoDetalhadoDTO> itensDetalhados = new ArrayList<>();

        for (var item : request.getItens()) {
            Produto produto = Optional.ofNullable(produtoMap.get(item.getProdutoId()))
                    .orElseThrow(() -> new ProdutoInvalidoException("Produto inválido: " + item.getProdutoId()));

            BigDecimal subtotalItem = produto.getPreco()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()))
                    .setScale(2, RoundingMode.HALF_UP);

            valorSubtotal = valorSubtotal.add(subtotalItem);
            quantidadeItens += item.getQuantidade();

            itensDetalhados.add(new ItemCalculoDetalhadoDTO(
                    produto.getId(),
                    produto.getNome(),
                    produto.getDescricao(),
                    item.getQuantidade(),
                    produto.getPreco(),
                    subtotalItem,
                    produto.isDisponivel()
            ));
        }

        BigDecimal taxaEntrega = Optional.ofNullable(restaurante.getTaxaEntrega()).orElse(BigDecimal.ZERO);
        BigDecimal valorTotal = valorSubtotal.add(taxaEntrega).setScale(2, RoundingMode.HALF_UP);

        return new ResultadoCalculoDetalhado(valorSubtotal, taxaEntrega, valorTotal, quantidadeItens, itensDetalhados);
    }

    private CalculoPedidoRequestDTO.ItemCalculoDTO criarItemCalculo(Long produtoId, Integer quantidade) {
        var item = new CalculoPedidoRequestDTO.ItemCalculoDTO();
        item.setProdutoId(produtoId);
        item.setQuantidade(quantidade);
        return item;
    }

    private CalculoPedidoRequestDTO criarRequest(Long restauranteId, Produto produto, int quantidade) {
        var request = new CalculoPedidoRequestDTO();
        request.setRestauranteId(restauranteId);
        request.setItens(List.of(criarItemCalculo(produto.getId(), quantidade)));
        return request;
    }

    // =====================================
    // RECORDS (Java 21)
    // =====================================

    private record ResultadoCalculo(
            BigDecimal valorSubtotal,
            BigDecimal taxaEntrega,
            BigDecimal valorTotal,
            Integer quantidadeItens
    ) {}

    private record ResultadoCalculoDetalhado(
            BigDecimal valorSubtotal,
            BigDecimal taxaEntrega,
            BigDecimal valorTotal,
            Integer quantidadeItens,
            List<ItemCalculoDetalhadoDTO> itensDetalhados
    ) {}
}
