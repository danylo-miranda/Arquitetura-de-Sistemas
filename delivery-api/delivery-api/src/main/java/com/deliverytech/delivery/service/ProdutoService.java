package com.deliverytech.delivery.service;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.repository.IProdutoRepository;


@Service
public class ProdutoService implements IProduto {
 
    @Autowired
    private IProdutoRepository repository;
 
    @Override
    public ProdutoDTO create(ProdutoDTO produtoDTO) {
       ModelMapper mapper = new ModelMapper();
 
       Produto entity = mapper.map(produtoDTO, Produto.class);
       Produto produto = repository.save(entity);
       ProdutoDTO dto = mapper.map(produto, ProdutoDTO.class);
 
       return dto;
    }
 
    @Override
    public List<ProdutoDTO> findAll() {
        ModelMapper mapper = new ModelMapper();  
        List<Produto> produto = repository.findAll();
 
        List<ProdutoDTO> ProdutoDtoList = Arrays.asList(mapper.map(produto, ProdutoDTO[].class));
 
        return ProdutoDtoList;
    }
    
    @Override
    public ProdutoDTO findById(Long id) {
        ModelMapper mapper = new ModelMapper();
        Produto produto = repository.findById(id).orElse(null);
        if (produto == null) {
            return null;
        }
        return mapper.map(produto, ProdutoDTO.class);
    }
    
    @Override
    public ProdutoDTO update(Long id, ProdutoDTO produtoDTO) {
        ModelMapper mapper = new ModelMapper();
        Produto produto = repository.findById(id).orElse(null);
        if (produto == null) {
            return null;
        }
        
        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        
        Produto updatedProduto = repository.save(produto);
        return mapper.map(updatedProduto, ProdutoDTO.class);
    }
    
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}