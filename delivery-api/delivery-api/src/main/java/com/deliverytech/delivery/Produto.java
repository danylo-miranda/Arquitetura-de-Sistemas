package com.deliverytech.delivery;

public class Produto {
    private String nome;
    private Double preco;
    private Cliente clienteComprador; // Relationship with Cliente

    public Produto() {
        // Creating a client as an example
        this.clienteComprador = new Cliente("Danylo", 31, 1.75, 70);
    }

    public Produto(String nome, Double preco, Cliente cliente) {
        this.nome = nome;
        this.preco = preco;
        this.clienteComprador = cliente;
    }

    // Getters and setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public Cliente getClienteComprador() { return clienteComprador; }
    public void setClienteComprador(Cliente clienteComprador) { 
        this.clienteComprador = clienteComprador; 
    }
}
