package com.deliverytech.delivery.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.entity.Cliente;
import com.deliverytech.delivery.repository.IClienteRepository;

@Service
public class ClienteService {
    
    @Autowired
    private IClienteRepository clienteRepository;
    
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        return toDTO(cliente);
    }
    
    public ClienteDTO create(ClienteDTO clienteDTO) {
        // Verificar se email já existe
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + clienteDTO.getEmail());
        }
        
        Cliente cliente = toEntity(clienteDTO);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return toDTO(clienteSalvo);
    }
    
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        
        // Verificar se o email foi alterado e se já existe
        if (!clienteExistente.getEmail().equals(clienteDTO.getEmail()) && 
            clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + clienteDTO.getEmail());
        }
        
        // Atualiza os campos
        clienteExistente.setNome(clienteDTO.getNome());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setTelefone(clienteDTO.getTelefone());
        clienteExistente.setEndereco(clienteDTO.getEndereco());
        
        // Verifica se ativo é null e define valor padrão
        //Boolean ativo = clienteDTO.getAtivo() != null ? clienteDTO.getAtivo() : true;
        //clienteExistente.setAtivo(ativo);
        
        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return toDTO(clienteAtualizado);
    }
    
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
    
    // Métodos adicionais
    public List<ClienteDTO> findByAtivoTrue() {
        return clienteRepository.findByAtivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public ClienteDTO findByEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com email: " + email));
        return toDTO(cliente);
    }
    
    // Converter Entidade para DTO
    private ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.getEndereco()
        );
    }
    
    // Converter DTO para Entidade
    private Cliente toEntity(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());
        cliente.setEndereco(clienteDTO.getEndereco());
        
        // Define valor padrão para ativo se for null
        //Boolean ativo = clienteDTO.getAtivo() != null ? clienteDTO.getAtivo() : true;
        //cliente.setAtivo(ativo);
        
        return cliente;
    }
}