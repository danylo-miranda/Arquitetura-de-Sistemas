package com.deliverytech.delivery.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery.dto.CalculoPedidoDetalhadoResponseDTO;
import com.deliverytech.delivery.dto.CalculoPedidoRequestDTO;
import com.deliverytech.delivery.dto.CalculoPedidoResponseDTO;
import com.deliverytech.delivery.dto.ItemCalculoDetalhadoDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.entity.Restaurante;
import com.deliverytech.delivery.repository.ICalculoPedidoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculoPedidoService {

    private final ICalculoPedidoRepository calculoPedidoRepository;

    /**
     * Calcula o valor total do pedido de forma simples
     */
    public CalculoPedidoResponseDTO calcularPedido(CalculoPedidoRequestDTO request) {
        log.info("Calculando pedido para restaurante: {}", request.getRestauranteId());
        
        // Validar restaurante
        Restaurante restaurante = calculoPedidoRepository
                .findRestauranteAtivoComTaxa(request.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado ou inativo"));

        // Extrair IDs dos produtos
        List<Long> produtoIds = request.getItens().stream()
                .map(CalculoPedidoRequestDTO.ItemCalculoDTO::getProdutoId)
                .collect(Collectors.toList());

        // Buscar produtos disponíveis
        List<Produto> produtos = calculoPedidoRepository.findProdutosDisponiveisByIds(produtoIds);
        
        // Verificar se todos os produtos pertencem ao restaurante
        validarProdutosDoRestaurante(produtoIds, restaurante.getId(), produtos);

        // Calcular valores
        ResultadoCalculo resultado = calcularValores(request, produtos, restaurante);

        return new CalculoPedidoResponseDTO(
                resultado.getValorSubtotal(),
                resultado.getTaxaEntrega(),
                resultado.getValorTotal(),
                resultado.getQuantidadeItens(),
                restaurante.getNome()
        );
    }

    /**
     * Calcula o pedido com detalhes de cada item
     */
    public CalculoPedidoDetalhadoResponseDTO calcularPedidoDetalhado(CalculoPedidoRequestDTO request) {
        log.info("Calculando pedido detalhado para restaurante: {}", request.getRestauranteId());
        
        // Validar restaurante
        Restaurante restaurante = calculoPedidoRepository
                .findRestauranteAtivoComTaxa(request.getRestauranteId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado ou inativo"));

        // Extrair IDs dos produtos
        List<Long> produtoIds = request.getItens().stream()
                .map(CalculoPedidoRequestDTO.ItemCalculoDTO::getProdutoId)
                .collect(Collectors.toList());

        // Buscar produtos disponíveis
        List<Produto> produtos = calculoPedidoRepository.findProdutosDisponiveisByIds(produtoIds);
        
        // Verificar se todos os produtos pertencem ao restaurante
        validarProdutosDoRestaurante(produtoIds, restaurante.getId(), produtos);

        // Criar mapa de produtos para acesso rápido
        Map<Long, Produto> produtoMap = produtos.stream()
                .collect(Collectors.toMap(Produto::getId, produto -> produto));

        // Calcular valores e itens detalhados
        ResultadoCalculoDetalhado resultado = calcularValoresDetalhados(request, produtoMap, restaurante);

        return new CalculoPedidoDetalhadoResponseDTO(
                resultado.getValorSubtotal(),
                resultado.getTaxaEntrega(),
                resultado.getValorTotal(),
                resultado.getQuantidadeItens(),
                restaurante.getNome(),
                restaurante.getCategoria(),
                resultado.getItensDetalhados()
        );
    }

    /**
     * Simula vários cenários de pedido para comparação
     */
    public List<CalculoPedidoResponseDTO> simularMultiplosCenarios(Long restauranteId) {
        log.info("Simulando múltiplos cenários para restaurante: {}", restauranteId);
        
        Restaurante restaurante = calculoPedidoRepository
                .findRestauranteAtivoComTaxa(restauranteId)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado ou inativo"));

        List<Produto> produtosPopulares = calculoPedidoRepository
                .findProdutosPopularesPorRestaurante(restauranteId);

        List<CalculoPedidoResponseDTO> simulacoes = new ArrayList<>();

        // Cenário 1: Pedido mínimo (1 produto)
        if (!produtosPopulares.isEmpty()) {
            CalculoPedidoRequestDTO request1 = new CalculoPedidoRequestDTO();
            request1.setRestauranteId(restauranteId);
            request1.setItens(List.of(
                    criarItemCalculo(produtosPopulares.get(0).getId(), 1)
            ));
            simulacoes.add(calcularPedido(request1));
        }

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

    // Métodos auxiliares privados
    private void validarProdutosDoRestaurante(List<Long> produtoIds, Long restauranteId, List<Produto> produtosEncontrados) {
        // Verificar se todos os produtos foram encontrados
        if (produtosEncontrados.size() != produtoIds.size()) {
            List<Long> idsEncontrados = produtosEncontrados.stream()
                    .map(Produto::getId)
                    .collect(Collectors.toList());
            
            List<Long> idsNaoEncontrados = produtoIds.stream()
                    .filter(id -> !idsEncontrados.contains(id))
                    .collect(Collectors.toList());
            
            throw new RuntimeException("Produtos não encontrados ou indisponíveis: " + idsNaoEncontrados);
        }

        // Verificar se produtos pertencem ao restaurante
        Long countProdutosRestaurante = calculoPedidoRepository
                .countProdutosDoRestaurante(produtoIds, restauranteId);
        
        if (countProdutosRestaurante != produtoIds.size()) {
            throw new RuntimeException("Alguns produtos não pertencem ao restaurante selecionado");
        }
    }

    private ResultadoCalculo calcularValores(CalculoPedidoRequestDTO request, List<Produto> produtos, Restaurante restaurante) {
        BigDecimal valorSubtotal = BigDecimal.ZERO;
        int quantidadeItens = 0;

        // Criar mapa de produtos para acesso rápido
        Map<Long, Produto> produtoMap = produtos.stream()
                .collect(Collectors.toMap(Produto::getId, produto -> produto));

        // Calcular subtotal
        for (CalculoPedidoRequestDTO.ItemCalculoDTO item : request.getItens()) {
            Produto produto = produtoMap.get(item.getProdutoId());
            BigDecimal subtotalItem = produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            valorSubtotal = valorSubtotal.add(subtotalItem);
            quantidadeItens += item.getQuantidade();
        }

        BigDecimal taxaEntrega = restaurante.getTaxaEntrega() != null ? restaurante.getTaxaEntrega() : BigDecimal.ZERO;
        BigDecimal valorTotal = valorSubtotal.add(taxaEntrega);

        return new ResultadoCalculo(valorSubtotal, taxaEntrega, valorTotal, quantidadeItens);
    }

    private ResultadoCalculoDetalhado calcularValoresDetalhados(CalculoPedidoRequestDTO request, 
                                                               Map<Long, Produto> produtoMap, 
                                                               Restaurante restaurante) {
        BigDecimal valorSubtotal = BigDecimal.ZERO;
        int quantidadeItens = 0;
        List<ItemCalculoDetalhadoDTO> itensDetalhados = new ArrayList<>();

        // Calcular subtotal e criar itens detalhados
        for (CalculoPedidoRequestDTO.ItemCalculoDTO item : request.getItens()) {
            Produto produto = produtoMap.get(item.getProdutoId());
            BigDecimal subtotalItem = produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            valorSubtotal = valorSubtotal.add(subtotalItem);
            quantidadeItens += item.getQuantidade();

            // Criar item detalhado
            ItemCalculoDetalhadoDTO itemDetalhado = new ItemCalculoDetalhadoDTO(
                    produto.getId(),
                    produto.getNome(),
                    produto.getDescricao(),
                    item.getQuantidade(),
                    produto.getPreco(),
                    subtotalItem,
                    produto.isDisponivel()
            );
            itensDetalhados.add(itemDetalhado);
        }

        BigDecimal taxaEntrega = restaurante.getTaxaEntrega() != null ? restaurante.getTaxaEntrega() : BigDecimal.ZERO;
        BigDecimal valorTotal = valorSubtotal.add(taxaEntrega);

        return new ResultadoCalculoDetalhado(valorSubtotal, taxaEntrega, valorTotal, quantidadeItens, itensDetalhados);
    }

    private CalculoPedidoRequestDTO.ItemCalculoDTO criarItemCalculo(Long produtoId, Integer quantidade) {
        CalculoPedidoRequestDTO.ItemCalculoDTO item = new CalculoPedidoRequestDTO.ItemCalculoDTO();
        item.setProdutoId(produtoId);
        item.setQuantidade(quantidade);
        return item;
    }

    // Classes auxiliares para encapsular resultados
    private static class ResultadoCalculo {
        private final BigDecimal valorSubtotal;
        private final BigDecimal taxaEntrega;
        private final BigDecimal valorTotal;
        private final Integer quantidadeItens;

        public ResultadoCalculo(BigDecimal valorSubtotal, BigDecimal taxaEntrega, 
                               BigDecimal valorTotal, Integer quantidadeItens) {
            this.valorSubtotal = valorSubtotal;
            this.taxaEntrega = taxaEntrega;
            this.valorTotal = valorTotal;
            this.quantidadeItens = quantidadeItens;
        }

        public BigDecimal getValorSubtotal() { return valorSubtotal; }
        public BigDecimal getTaxaEntrega() { return taxaEntrega; }
        public BigDecimal getValorTotal() { return valorTotal; }
        public Integer getQuantidadeItens() { return quantidadeItens; }
    }

    private static class ResultadoCalculoDetalhado extends ResultadoCalculo {
        private final List<ItemCalculoDetalhadoDTO> itensDetalhados;

        public ResultadoCalculoDetalhado(BigDecimal valorSubtotal, BigDecimal taxaEntrega, 
                                        BigDecimal valorTotal, Integer quantidadeItens,
                                        List<ItemCalculoDetalhadoDTO> itensDetalhados) {
            super(valorSubtotal, taxaEntrega, valorTotal, quantidadeItens);
            this.itensDetalhados = itensDetalhados;
        }

        public List<ItemCalculoDetalhadoDTO> getItensDetalhados() { return itensDetalhados; }
    }
}