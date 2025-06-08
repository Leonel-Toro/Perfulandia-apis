package com.clientes_api.services;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.models.Cliente;
import com.clientes_api.repository.ClienteRepository;
import com.usuarios_api.dto.RegistroUsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    public void registrarCliente(RegistroClienteDTO request) {
        List<String> errores = new ArrayList<>();

        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            errores.add("Nombre: El nombre no debe estar vacío.");
        }

        if (request.getApellido() == null || request.getApellido().trim().isEmpty()) {
            errores.add("Apellido: El apellido no debe estar vacío.");
        }

        if (request.getTelefono() == null || !request.getTelefono().matches("^\\d{9}$")) {
            errores.add("Teléfono: Debe contener 9 dígitos.");
        }

        if (request.getRut() == null || request.getRut().trim().isEmpty()) {
            errores.add("RUT: El RUT no es válido.");
        }

        if (request.getDireccion() == null || request.getDireccion().trim().isEmpty()) {
            errores.add("Dirección: No debe estar vacía.");
        }

        if (request.getCorreo() == null || request.getCorreo().trim().isEmpty()) {
            errores.add("Correo: El correo es obligatorio.");
        }

        if (request.getPassword() == null || request.getPassword().trim().length() < 8) {
            errores.add("Contraseña: Debe tener al menos 8 caracteres.");
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errores));
        }

        // Crear entidad cliente
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setRut(request.getRut());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());

        // Crear usuario asociado
        RegistroUsuarioRequest newUser = new RegistroUsuarioRequest();
        newUser.setCorreo(request.getCorreo());
        newUser.setPassword(request.getPassword());
        newUser.setRol("CLIENTE");

        try {
            restTemplate.postForEntity("http://localhost:8081/", newUser, Void.class);
            clienteRepository.save(cliente);
        } catch (HttpClientErrorException error) {
            // Lanza el mensaje de error tal como lo devuelve el microservicio de usuarios
            throw new RuntimeException("Error al crear usuario: " + error.getResponseBodyAsString());
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
