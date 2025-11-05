package com.deliverytech.delivery.exception;

public class RestauranteNaoEncontradoException extends RuntimeException {
    public RestauranteNaoEncontradoException(Long id) {
        super("Restaurante não encontrado ou inativo: " + id);
    }
}
