package com.clientes_api.services;

import com.clientes_api.models.Cliente;
import com.clientes_api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    public Cliente getById(Integer id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.orElse(null);
    }

    public Cliente add(Cliente Cliente) {
        return clienteRepository.save(Cliente);
    }

   
    public Cliente update(Integer id, Cliente Cliente) {
        if (clienteRepository.existsById(id)) {
            Cliente.setId(id); 
            return clienteRepository.save(Cliente);
        }
        return null; // No se encontró la Cliente
    }

    public Cliente delete(Integer id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            clienteRepository.deleteById(id);
            return cliente.get();
        }
        return null;
    }
}
