package com.deliverytech.delivery.service;

import java.util.List;

import com.deliverytech.delivery.dto.ProdutoDTO;
import com.deliverytech.delivery.dto.ProdutoResponseDTO;

/**
 * Interface para o serviço de gerenciamento de produtos
 */
public interface IProdutoService {
    
    /**
     * Cadastra um novo produto
     * @param dto Dados do produto a ser cadastrado
     * @return ProdutoResponseDTO com os dados do produto cadastrado
     */
    ProdutoResponseDTO cadastrarProduto(ProdutoDTO dto);
    
    /**
     * Busca um produto pelo ID
     * @param id ID do produto
     * @return ProdutoResponseDTO com os dados do produto
     */
    ProdutoResponseDTO buscarProdutoPorId(Long id);
    
    /**
     * Atualiza um produto existente
     * @param id ID do produto a ser atualizado
     * @param dto Dados atualizados do produto
     * @return ProdutoResponseDTO com os dados do produto atualizado
     */
    ProdutoResponseDTO atualizarProduto(Long id, ProdutoDTO dto);
    
    /**
     * Remove um produto
     * @param id ID do produto a ser removido
     */
    void removerProduto(Long id);
    
    /**
     * Altera a disponibilidade de um produto (ativa/desativa)
     * @param id ID do produto
     * @return ProdutoResponseDTO com a disponibilidade atualizada
     */
    ProdutoResponseDTO alterarDisponibilidade(Long id);
    
    /**
     * Busca produtos por categoria e que estão disponíveis
     * @param categoria Categoria dos produtos
     * @return Lista de produtos da categoria que estão disponíveis
     */
    List<ProdutoResponseDTO> buscarProdutosPorCategoria(String categoria);
    
    /**
     * Busca produtos por nome (busca parcial) e que estão disponíveis
     * @param nome Nome ou parte do nome do produto
     * @return Lista de produtos que correspondem ao nome e estão disponíveis
     */
    List<ProdutoResponseDTO> buscarProdutosPorNome(String nome);
}