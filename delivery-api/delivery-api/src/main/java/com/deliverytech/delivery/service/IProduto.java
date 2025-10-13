package com.deliverytech.delivery.service;

import java.util.List;

import com.deliverytech.delivery.dto.ProdutoDTO;

public interface IProduto {
    List<ProdutoDTO> findAll();
    ProdutoDTO findById(Long id);
    ProdutoDTO create(ProdutoDTO produtoDTO);
    ProdutoDTO update(Long id, ProdutoDTO produtoDTO);

    void delete(Long id);
}
