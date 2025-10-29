package com.deliverytech.delivery.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.CalculoPedidoRequestDTO;
import com.deliverytech.delivery.dto.CalculoPedidoResponseDTO;
import com.deliverytech.delivery.dto.PedidoDTO;
import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.service.IPedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
 
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
@Autowired
    private IPedidoService pedidoService;
 
    @PostMapping
    @Operation(summary = "Criar pedido",
               description = "Cria um novo pedido no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente ou restaurante não encontrado"),
        @ApiResponse(responseCode = "409", description = "Produto indisponível")
    })
    public ResponseEntity<PedidoDTO> criarPedido(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do pedido a ser criado"
            ) PedidoDTO dto) {
 
        PedidoDTO pedido = pedidoService.criarPedido(dto);
 
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID",
               description = "Recupera um pedido específico com todos os detalhes")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoDTO> buscarPorId(
            @Parameter(description = "ID do pedido")
            @PathVariable Long id) {
        PedidoDTO pedido = pedidoService.buscarPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }
 
    @GetMapping
    @Operation(summary = "Listar pedidos",
               description = "Lista pedidos com filtros opcionais e paginação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    public ResponseEntity<List<PedidoDTO>> listar(
            @Parameter(description = "Status do pedido")
            @RequestParam(required = false) StatusPedido status,
            @Parameter(description = "Data inicial")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @Parameter(description = "Parâmetros de paginação")
            Pageable pageable) {
 
            List<PedidoDTO> pedidos = pedidoService.listarPedidos(status, dataInicio, dataFim, pageable);
        return ResponseEntity.ok(pedidos);
    }
 
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido",
               description = "Atualiza o status de um pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Transição de status inválida")
    })
    public ResponseEntity<PedidoDTO> atualizarStatus(
            @Parameter(description = "ID do pedido")
            @PathVariable Long id,
            @Valid @RequestBody StatusPedido status) {
 
        PedidoDTO pedido = pedidoService.atualizarStatusPedido(id, status);
 
        return ResponseEntity.ok(pedido);
    }
 
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido",
               description = "Cancela um pedido se possível")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Pedido não pode ser cancelado")
    })
    public ResponseEntity<Void> cancelarPedido(
            @Parameter(description = "ID do pedido")
            @PathVariable Long id) {
 
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
 
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Histórico do cliente",
               description = "Lista todos os pedidos de um cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<PedidoDTO>> buscarPorCliente(
            @Parameter(description = "ID do cliente")
            @PathVariable Long clienteId) {
 
        List<PedidoDTO> pedidos = pedidoService.buscarPedidosPorCliente(clienteId);
 
        return ResponseEntity.ok(pedidos);
    }
 
    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Pedidos do restaurante",
               description = "Lista todos os pedidos de um restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<List<PedidoDTO>> buscarPorRestaurante(
            @Parameter(description = "ID do restaurante")
            @PathVariable Long restauranteId,
            @Parameter(description = "Status do pedido")
            @RequestParam(required = false) StatusPedido status) {
 
        List<PedidoDTO> pedidos =
            pedidoService.buscarPedidosPorRestaurante(restauranteId, status);
 
        return ResponseEntity.ok(pedidos);
    }
 
    @PostMapping("/calcular")
    @Operation(summary = "Calcular total do pedido",
               description = "Calcula o total de um pedido sem salvá-lo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total calculado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<CalculoPedidoResponseDTO> calcularTotal(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Itens para cálculo"
            ) CalculoPedidoRequestDTO dto) {
        CalculoPedidoResponseDTO calculo = pedidoService.calcularTotalPedido(dto);
        return ResponseEntity.ok(calculo);
    }
}