package com.perfulandia.envios_api.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.envios_api.model.Envios;
import com.perfulandia.envios_api.services.EnviosService;

@RestController
@RequestMapping("/api/envios")
public class EnviosController {

    @Autowired
    private EnviosService enviosService;

    // Método para obtener todos los envíos
    @GetMapping({"","/"})
    public ResponseEntity<List<Envios>> getAll() {
        return ResponseEntity.ok(enviosService.getAll());
    }

    // Método para obtener un envío por su ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Envios envio = enviosService.getById(id);
        if (envio != null) {
            return ResponseEntity.ok(envio);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Método para guardar un nuevo envío
    @PostMapping({"","/"})
    public ResponseEntity<Envios> add(@RequestBody Envios envio) {
        Envios nuevoEnvio = enviosService.add(envio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEnvio);
    }

    // Método para actualizar un envío existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Envios envio) {
        Envios actualizado = enviosService.update(id, envio);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Envio no encontrado");
        }
    }

    // Método para eliminar un envío por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Envios eliminado = enviosService.delete(id);
        if (eliminado != null) {
            return ResponseEntity.ok(eliminado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Envio no encontrado");
        }
    }

    // Nuevo endpoint: Crear envío y completar dirección desde microservicio cliente
    @PostMapping("/nuevo-con-cliente/{idCliente}")
    public ResponseEntity<?> crearEnvioConCliente(@PathVariable Integer idCliente, @RequestBody Envios envio) {
        try {
            Envios nuevo = enviosService.crearEnvioConCliente(idCliente, envio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
