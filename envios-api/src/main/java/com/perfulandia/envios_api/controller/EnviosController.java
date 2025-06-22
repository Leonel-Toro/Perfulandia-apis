package com.perfulandia.envios_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.envios_api.models.Envios;
import com.perfulandia.envios_api.services.EnviosService;

@RestController
@RequestMapping("/envios")
public class EnviosController {

    @Autowired
    private EnviosService enviosService;

    // GET /envios -> listar todos
    @GetMapping
    public List<Envios> getAllEnvios() {
        return enviosService.getAll();
    }

    // GET /envios/{id} -> obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<Envios> getEnvioById(@PathVariable int id) {
        Envios envio = enviosService.getById(id);
        if (envio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(envio);
    }

    // POST /envios -> crear nuevo
    @PostMapping
    public Envios saveEnvio(@RequestBody Envios envio) {
        return enviosService.save(envio);
    }

    // PUT /envios/{id} -> actualizar por id
    @PutMapping("/{id}")
    public ResponseEntity<Envios> updateEnvio(@PathVariable Long id, @RequestBody Envios envio) {
        Envios updatedEnvio = enviosService.update(id, envio);
        if (updatedEnvio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEnvio);
    }

    // DELETE /envios/{id} -> eliminar por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Envios> deleteEnvio(@PathVariable Long id) {
        Envios deletedEnvio = enviosService.delete(id);
        if (deletedEnvio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deletedEnvio);
    }
}
