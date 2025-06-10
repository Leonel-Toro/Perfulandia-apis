package com.perfulandia.envios_api.services;

import com.perfulandia.envios_api.model.Envios;
import com.perfulandia.envios_api.repository.EnviosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnviosService {

    @Autowired
    private EnviosRepository enviosRepository;
    // Método para obtener todos los envíos
    public List<Envios> getAll() {
        return enviosRepository.findAll();
    }

    // Método para obtener un envío por su ID
    public Envios getById(Integer id) {
        Optional<Envios> Envio = enviosRepository.findById(id);
        return Envio.orElse(null); // Devuelve null si no se encuentra el envío
    }

    // Método para guardar un nuevo envío
    public Envios add(Envios envio) {
        return enviosRepository.save(envio);
    }

    // Método para actualizar un envío existente
    public Envios update(Integer id, Envios envio) {
        if (enviosRepository.existsById(id)) {
            envio.setId(id); // Asegúrate de que el ID del envío a actualizar sea correcto
            return enviosRepository.save(envio);
        }
        return null; // Devuelve null si no se encuentra el envío
    }

    // Método para eliminar un envío por su ID
    public Envios delete(Integer id) {
        Optional<Envios> envio = enviosRepository.findById(id);
        if (envio.isPresent()) {
            enviosRepository.deleteById(id);
            return envio.get(); // Devuelve el envío eliminado
        }
        return null; // Devuelve null si no se encuentra el envío
    }


}
