package com.deliverytech.delivery.service;

import java.util.Arrays;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;
import com.deliverytech.delivery.entity.Produto;
import com.deliverytech.delivery.exception.BusinessException;
import com.deliverytech.delivery.exception.EntityNotFoundException;
import com.deliverytech.delivery.repository.IProdutoRepository;
import com.deliverytech.delivery.repository.IRestauranteRepository;

 
@Service
public class ProdutoService implements IProdutoService {
    @Autowired
    private IProdutoRepository repositoryProduto;
    @Autowired
    private IRestauranteRepository repositoryRestaurante;
    @Autowired
    private ModelMapper mapper;
 
    @Override
    public ProdutoResponseDTO cadastrarProduto(ProdutoDTO dto) {
        // Validar se restaurante associado existe
        if (!repositoryRestaurante.existsById(dto.getIdRestaurante())) {
            throw new BusinessException("Restaurante não cadastrado: " + dto.getIdRestaurante());
        }
        // Validar se produto com mesmo nome ja existe na base
        if (repositoryProduto.existsByNome(dto.getNome())) {
            throw new BusinessException("Produto com mesmo nome existente: " + dto.getNome());
        }
        Produto entity = mapper.map(dto, Produto.class);
        Produto entityCadastrada = repositoryProduto.save(entity);
        ProdutoResponseDTO response = mapper.map(entityCadastrada, ProdutoResponseDTO.class);
        return response;
    }
 
    @Override
    public ProdutoResponseDTO buscarProdutoPorId(Long id) {
       Produto produto = repositoryProduto.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
       return mapper.map(produto, ProdutoResponseDTO.class);
    }
 
    @Override
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoDTO dto) {
        // Validar se produto a ser atualizado existe na base
        if (!repositoryProduto.existsById(id)) {
            throw new EntityNotFoundException("Produto não encontrado com ID: " + id);
        }        
        Produto entity = mapper.map(dto, Produto.class);
        entity.setId(id);
        Produto entityAtualizada = repositoryProduto.save(entity);
        return mapper.map(entityAtualizada, ProdutoResponseDTO.class);
    }
 
    @Override
    public void removerProduto(Long id) {
       Produto produto = repositoryProduto.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
       repositoryProduto.delete(produto);    
    }
 
    @Override
    public ProdutoResponseDTO alterarDisponibilidade(Long id) {
        Produto produto = repositoryProduto.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
        produto.setDisponivel(!produto.isDisponivel());
        Produto entityAtualizada = repositoryProduto.save(produto);
        return mapper.map(entityAtualizada, ProdutoResponseDTO.class);
    }
 
    @Override
    public List<ProdutoResponseDTO> buscarProdutosPorCategoria(String categoria) {
        List<Produto> produtos = repositoryProduto.findByCategoriaAndDisponivelTrue(categoria);
        return Arrays.asList(mapper.map(produtos, ProdutoResponseDTO[].class));
    }
 
    @Override
    public List<ProdutoResponseDTO> buscarProdutosPorNome(String nome) {
        List<Produto> produtos = repositoryProduto.findByNomeContainingIgnoreCaseAndDisponivelTrue(nome);
        return Arrays.asList(mapper.map(produtos, ProdutoResponseDTO[].class));
    }
 
}