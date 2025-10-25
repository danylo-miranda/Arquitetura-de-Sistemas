package com.deliverytech.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.entity.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {
 
    // Buscar cliente por email (método derivado)
    Optional<Cliente> findByEmail(String email);
 
    // Verificar se email já existe
    boolean existsByEmail(String email);
 
    // Buscar clientes ativos
    List<Cliente> findByAtivoTrue();
 
    // Buscar clientes por nome (contendo)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
 
    // Buscar clientes por telefone
    Optional<Cliente> findByTelefone(String telefone);
 
}
 