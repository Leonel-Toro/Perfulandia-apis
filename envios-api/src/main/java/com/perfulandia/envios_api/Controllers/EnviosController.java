package com.perfulandia.envios_api.Controllers;

import com.perfulandia.envios_api.model.Envios;
import com.perfulandia.envios_api.services.EnviosService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;


@RestController
@RequestMapping("/api/envios")
public class EnviosController {

    @Autowired
    private EnviosService enviosService;

    // Creamos una lista para simular una base de datos
    private List<Envios> envios = new ArrayList<>();
    public EnviosController() {
        envios.add(new Envios(1, "Enviado", null, "Transportista 1", "123456"));
        envios.add(new Envios(2, "En Ruta", null, "Transportista 2", "654321"));
        envios.add(new Envios(3, "Entregado", null, "Transportista 3", "789012"));
    }

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
        Envios Actualizado = enviosService.update(id, envio);
        if (Actualizado != null) {
            return ResponseEntity.ok(Actualizado);
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


}
