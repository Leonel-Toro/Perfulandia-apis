package com.perfulandia.envios_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.envios_api.models.Envios;
import com.perfulandia.envios_api.repository.EnviosRepository;

@Service
public class EnviosService {

    @Autowired
    private EnviosRepository enviosRepository;

    public List<Envios> getAll() {
        return enviosRepository.findAll();
    }

    public Envios getById(Long id) {
        Optional<Envios> envio = enviosRepository.findById(id);
        return envio.orElse(null);
    }

    public Envios save(Envios envio) {
        return enviosRepository.save(envio);
    }

    public Envios update(Long id, Envios envio) {
        if (enviosRepository.existsById(id)) {
            envio.setId(id);
            return enviosRepository.save(envio);
        }
        return null;
    }

    public Envios delete(Long id) {
        Optional<Envios> envio = enviosRepository.findById(id);
        if (envio.isPresent()) {
            enviosRepository.deleteById(id);
            return envio.get();
        }
        return null;
    }
}
