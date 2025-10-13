package com.deliverytech.delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.service.IProduto;



@RestController
@RequestMapping("/api/v1/produto")
public class ProdutoController {
        @Autowired
    private IProduto produtoService;
    
    // Construtor com log
    public ProdutoController() {
        System.out.println("ProdutoController inicializado!");
    }
 
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> list() {
        List<ProdutoDTO> produtos = produtoService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(produtos);
    }
 
}