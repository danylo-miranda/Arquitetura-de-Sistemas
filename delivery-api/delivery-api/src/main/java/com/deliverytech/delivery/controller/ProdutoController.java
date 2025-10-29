package com.deliverytech.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.service.IProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
 
@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
public class ProdutoController {
    @Autowired
    private IProdutoService produtoService;
 
    @PostMapping
    @Operation(summary = "Cadastrar produto",
               description = "Cria um novo produto no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> cadastrar(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Dados do produto a ser criado"
            ) ProdutoDTO dto) {
 
        ProdutoResponseDTO produto = produtoService.cadastrarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID",
               description = "Recupera um produto específico pelo ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(
            @Parameter(description = "ID do produto")
            @PathVariable Long id) {
 
        ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto);
    }
 
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto",
               description = "Atualiza os dados de um produto existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @Parameter(description = "ID do produto")
            @PathVariable Long id,
            @Valid @RequestBody ProdutoDTO dto) {
 
        ProdutoResponseDTO produto = produtoService.atualizarProduto(id, dto);
        return ResponseEntity.ok(produto);
    }
 
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto",
               description = "Remove um produto do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "409", description = "Produto possui pedidos associados")
    })
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID do produto")
            @PathVariable Long id) {
 
        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }
 
    @PatchMapping("/{id}/disponibilidade")
    @Operation(summary = "Alterar disponibilidade",
               description = "Alterna a disponibilidade do produto")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disponibilidade alterada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> alterarDisponibilidade(
            @Parameter(description = "ID do produto")
            @PathVariable Long id) {
 
        ProdutoResponseDTO produto = produtoService.alterarDisponibilidade(id);
        return ResponseEntity.ok(produto);
    }
 
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar por categoria",
               description = "Lista produtos de uma categoria específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorCategoria(
            @Parameter(description = "Categoria do produto")
            @PathVariable String categoria) {
 
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }
 
    @GetMapping("/buscar")
    @Operation(summary = "Buscar por nome",
               description = "Busca produtos pelo nome")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(
            @Parameter(description = "Nome do produto")
            @RequestParam String nome) {
 
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorNome(nome);
        return ResponseEntity.ok(produtos);
    }
}