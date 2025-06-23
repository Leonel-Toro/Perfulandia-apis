package com.perfulandia.envios_api.services;

import com.perfulandia.envios_api.dto.ClienteDTO;
import com.perfulandia.envios_api.model.Envios;
import com.perfulandia.envios_api.repository.EnviosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class EnviosService {

    @Autowired
    private EnviosRepository enviosRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Obtener todos los envíos
    public List<Envios> getAll() {
        return enviosRepository.findAll();
    }

    // Obtener por ID
    public Envios getById(Integer id) {
        return enviosRepository.findById(id).orElse(null);
    }

    // Crear envío usando ID cliente
    public Envios crearEnvioConCliente(Integer idCliente, Envios envio) {
        String urlCliente = "http://localhost:8081/api/cliente/" + idCliente;
        ClienteDTO cliente = restTemplate.getForObject(urlCliente, ClienteDTO.class);

        if (cliente == null || cliente.getDireccion() == null) {
            throw new RuntimeException("Cliente no encontrado o sin dirección.");
        }

        envio.setDireccion(cliente.getDireccion());
        envio.setEstado("Pendiente");
        envio.setTracking("TRK-" + System.currentTimeMillis());
        envio.setTransportista("Pendiente");

        return enviosRepository.save(envio);
    }

    // Agregar normal
    public Envios add(Envios envio) {
        return enviosRepository.save(envio);
    }

    // Actualizar
    public Envios update(Integer id, Envios envio) {
        if (enviosRepository.existsById(id)) {
            envio.setId(id);
            return enviosRepository.save(envio);
        }
        return null;
    }

    // Eliminar
    public Envios delete(Integer id) {
        Optional<Envios> envio = enviosRepository.findById(id);
        if (envio.isPresent()) {
            enviosRepository.deleteById(id);
            return envio.get();
        }
        return null;
    }
}
