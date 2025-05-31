package com.clientes_api.services;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.models.Cliente;
import com.clientes_api.repository.ClienteRepository;
import com.usuarios_api.dto.RegistroUsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RestTemplate restTemplate;

    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    public Cliente getById(Integer id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.orElse(null);
    }

    public void registrarCliente(RegistroClienteDTO request){
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setRut(request.getRut());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());

        RegistroUsuarioRequest newUser = new RegistroUsuarioRequest();
        newUser.setCorreo(request.getCorreo());
        newUser.setPassword(request.getPassword());
        newUser.setRol("CLIENTE");

        try {
            restTemplate.postForEntity("http://localhost:8081/api/usuarios", newUser, Void.class);
            clienteRepository.save(cliente);
        }catch (HttpClientErrorException error){
            throw new RuntimeException(error.getResponseBodyAsString());
        }
    }

   
    public Cliente update(Long id, Cliente cliente) {
        if (clienteRepository.existsById(id.intValue())) {
            cliente.setId(id);
            return clienteRepository.save(cliente);
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
