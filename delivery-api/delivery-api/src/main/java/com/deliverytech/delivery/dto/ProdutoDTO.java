package com.deliverytech.delivery.dto;
 
import java.math.BigDecimal;
import com.deliverytech.delivery.entity.StatusProduto;

import lombok.Data;
 
@Data
public class ProdutoDTO {
    private Long id;
    private String name;
    private String description;
    private String categoria;
    private BigDecimal preco;
    private int estoque;
    private BigDecimal nota;
    private String imagem;
    private StatusProduto status;
}
