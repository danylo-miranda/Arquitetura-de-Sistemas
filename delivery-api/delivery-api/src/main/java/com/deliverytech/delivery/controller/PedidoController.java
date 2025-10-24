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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.PedidoDTO;
import com.deliverytech.delivery.dto.PedidoResponseDTO;
import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.service.IPedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos")
public class PedidoController {

    private final IPedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Criar um novo pedido", description = "Cria um novo pedido com os itens fornecidos")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Cliente, restaurante ou produto não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> create(
            @Parameter(description = "Dados do pedido a ser criado") 
            @Valid @RequestBody PedidoDTO pedidoDTO) {
        
        PedidoResponseDTO pedidoCriado = pedidoService.create(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCriado);
    }

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna todos os pedidos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    public ResponseEntity<List<PedidoResponseDTO>> findAll() {
        List<PedidoResponseDTO> pedidos = pedidoService.findAll();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna os detalhes de um pedido específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> findById(
            @Parameter(description = "ID do pedido") 
            @PathVariable Long id) {
        
        PedidoResponseDTO pedido = pedidoService.findById(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/numero/{numeroPedido}")
    @Operation(summary = "Buscar pedido por número", description = "Retorna os detalhes de um pedido pelo número único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> findByNumeroPedido(
            @Parameter(description = "Número único do pedido") 
            @PathVariable String numeroPedido) {
        
        PedidoResponseDTO pedido = pedidoService.findByNumeroPedido(numeroPedido);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente", description = "Retorna todos os pedidos de um cliente específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> findByClienteId(
            @Parameter(description = "ID do cliente") 
            @PathVariable Long clienteId) {
        
        List<PedidoResponseDTO> pedidos = pedidoService.findByClienteId(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Listar pedidos por restaurante", description = "Retorna todos os pedidos de um restaurante específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<List<PedidoResponseDTO>> findByRestauranteId(
            @Parameter(description = "ID do restaurante") 
            @PathVariable Long restauranteId) {
        
        List<PedidoResponseDTO> pedidos = pedidoService.findByRestauranteId(restauranteId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar pedidos por status", description = "Retorna todos os pedidos com um status específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    public ResponseEntity<List<PedidoResponseDTO>> findByStatus(
            @Parameter(description = "Status dos pedidos") 
            @PathVariable StatusPedido status) {
        
        List<PedidoResponseDTO> pedidos = pedidoService.findByStatus(status);
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Transição de status inválida"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> updateStatus(
            @Parameter(description = "ID do pedido") 
            @PathVariable Long id,
            @Parameter(description = "Novo status do pedido") 
            @RequestParam StatusPedido status) {
        
        PedidoResponseDTO pedidoAtualizado = pedidoService.updateStatus(id, status);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Não é possível cancelar o pedido no status atual"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(
            @Parameter(description = "ID do pedido") 
            @PathVariable Long id) {
        
        PedidoResponseDTO pedidoCancelado = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedidoCancelado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pedido", description = "Exclui um pedido específico (apenas pedidos cancelados)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Não é possível excluir o pedido no status atual"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do pedido") 
            @PathVariable Long id) {
        
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints adicionais para relatórios e estatísticas

    @GetMapping("/cliente/{clienteId}/ativos")
    @Operation(summary = "Verificar pedidos ativos do cliente", description = "Retorna se o cliente possui pedidos ativos")
    @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso")
    public ResponseEntity<Boolean> clienteTemPedidosAtivos(
            @Parameter(description = "ID do cliente") 
            @PathVariable Long clienteId) {
        
        List<PedidoResponseDTO> pedidosAtivos = pedidoService.findByClienteId(clienteId)
                .stream()
                .filter(pedido -> {
                    StatusPedido status = StatusPedido.valueOf(pedido.getStatus());
                    return status == StatusPedido.PENDENTE || 
                           status == StatusPedido.CONFIRMADO; 
                           //status == StatusPedido.EM_PREPARO;
                })
                .collect(java.util.stream.Collectors.toList());
        
        return ResponseEntity.ok(!pedidosAtivos.isEmpty());
    }

    @GetMapping("/restaurante/{restauranteId}/dashboard")
    @Operation(summary = "Dashboard do restaurante", description = "Retorna estatísticas de pedidos para o restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestauranteDashboardDTO> getDashboardRestaurante(
            @Parameter(description = "ID do restaurante") 
            @PathVariable Long restauranteId) {
        
        List<PedidoResponseDTO> pedidosRestaurante = pedidoService.findByRestauranteId(restauranteId);
        
        RestauranteDashboardDTO dashboard = new RestauranteDashboardDTO();
        dashboard.setTotalPedidos((long) pedidosRestaurante.size());
        dashboard.setPedidosPendentes(pedidosRestaurante.stream()
                .filter(p -> StatusPedido.PENDENTE.name().equals(p.getStatus()))
                .count());
        dashboard.setPedidosEmPreparo(pedidosRestaurante.stream()
                //.filter(p -> StatusPedido.EM_PREPARO.name().equals(p.getStatus()))
                .count());
        dashboard.setPedidosEntregues(pedidosRestaurante.stream()
                .filter(p -> StatusPedido.ENTREGUE.name().equals(p.getStatus()))
                .count());
        
        return ResponseEntity.ok(dashboard);
    }

    // DTO para dashboard do restaurante
    public static class RestauranteDashboardDTO {
        private Long totalPedidos;
        private Long pedidosPendentes;
        private Long pedidosEmPreparo;
        private Long pedidosEntregues;
        
        public RestauranteDashboardDTO() {}
        
        // Getters e Setters
        public Long getTotalPedidos() { return totalPedidos; }
        public void setTotalPedidos(Long totalPedidos) { this.totalPedidos = totalPedidos; }
        
        public Long getPedidosPendentes() { return pedidosPendentes; }
        public void setPedidosPendentes(Long pedidosPendentes) { this.pedidosPendentes = pedidosPendentes; }
        
        public Long getPedidosEmPreparo() { return pedidosEmPreparo; }
        public void setPedidosEmPreparo(Long pedidosEmPreparo) { this.pedidosEmPreparo = pedidosEmPreparo; }
        
        public Long getPedidosEntregues() { return pedidosEntregues; }
        public void setPedidosEntregues(Long pedidosEntregues) { this.pedidosEntregues = pedidosEntregues; }
    }
}