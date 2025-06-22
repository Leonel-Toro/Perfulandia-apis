package com.clientes_api.services;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.models.Cliente;
import com.clientes_api.repository.ClienteRepository;
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
            restTemplate.postForEntity("http://localhost:8081/api/usuarios/", newUser, Void.class);
            clienteRepository.save(cliente);
        } catch (HttpClientErrorException error) {
            // Lanza el mensaje de error tal como lo devuelve el microservicio de usuarios
            throw new RuntimeException("Error al crear usuario: " + error.getResponseBodyAsString());
        }
    }

    public Cliente actualizarCliente(Integer idCliente,Cliente cliente){
        List<String> errores = new ArrayList<>();
        // Crear entidad cliente
        Cliente clienteUp = new Cliente();
        if(clienteRepository.existsById(idCliente)){
            cliente.setId(idCliente.longValue());

            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                errores.add("Nombre: El nombre no debe estar vacío.");
            }

            if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
                errores.add("Apellido: El apellido no debe estar vacío.");
            }

            if (cliente.getTelefono() == null || !cliente.getTelefono().matches("^\\d{9}$")) {
                errores.add("Teléfono: Debe contener 9 dígitos.");
            }

            if (cliente.getRut() == null || cliente.getRut().trim().isEmpty()) {
                errores.add("RUT: El RUT no es válido.");
            }

            if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
                errores.add("Dirección: No debe estar vacía.");
            }

            if (!errores.isEmpty()) {
                throw new IllegalArgumentException(String.join("; ", errores));
            }

            clienteUp.setNombre(cliente.getNombre());
            clienteUp.setApellido(cliente.getApellido());
            clienteUp.setRut(cliente.getRut());
            clienteUp.setTelefono(cliente.getTelefono());
            clienteUp.setDireccion(cliente.getDireccion());
            clienteRepository.save(clienteUp);
        }
        return clienteUp;
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
