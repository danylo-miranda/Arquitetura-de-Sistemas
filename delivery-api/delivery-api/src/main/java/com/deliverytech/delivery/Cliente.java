package com.deliverytech.delivery;

public class Cliente {

    public Integer idade;
    public String nome;
    public Double  altura;
    public Integer peso;

    //construtor
    public Cliente(String nome, Integer idade, Double altura, Integer peso) {
        this.nome = nome;
        this.idade = idade;
        this.altura = altura;
        this.peso = peso;
    }

    //construtor padrão
    public Cliente() {}

    //getters and setters
    public Integer getIdade() {return idade;}
    public void setIdade(Integer idade) {this.idade = idade;}

    public String getNome() {return nome;}
    public void setIdade(String nome) {this.nome = nome;}

    public Double getAltura() {return altura;}
    public void setAltura(Double altura) {this.altura = altura;}

    public Integer getPeso() {return peso;}
    public void setPeso(Integer peso) {this.peso = peso;}

    //metodos
    public Integer somar(Integer A, Integer B) {
        return A + B;
    }

    public Double calcularIMC() {
        if (altura == null || altura == 0 || peso == null) {
            return 0.0;
        }
        return peso / (altura * altura);
    }
    @Override
    public String toString() {
        return "Cliente{" +
        "nome='" + nome + '\'' +
        ", idade=" + idade +
        ", altura=" + altura +
        ", peso=" + peso +
        '}';
    }
}
