package com.perfulandia.envios_api.Controllers;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


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

    // MÉTODOS HATEOAS

    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Envios>> getByIdHateoas(@PathVariable Integer id) {
        Envios envio = enviosService.getById(id);
        if (envio == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        EntityModel<Envios> model = EntityModel.of(envio,
            linkTo(methodOn(EnviosController.class).getByIdHateoas(id)).withSelfRel(),
            linkTo(methodOn(EnviosController.class).getAllHateoas()).withRel("todos-los-envios"),
            linkTo(methodOn(EnviosController.class).updateHateoas(id, null)).withRel("actualizar"),
            linkTo(methodOn(EnviosController.class).deleteHateoas(id)).withRel("eliminar")
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<Envios>>> getAllHateoas() {
        List<EntityModel<Envios>> envios = enviosService.getAll().stream()
            .map(envio -> EntityModel.of(envio,
                linkTo(methodOn(EnviosController.class).getByIdHateoas(envio.getId())).withSelfRel()
            ))
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Envios>> collection = CollectionModel.of(envios,
            linkTo(methodOn(EnviosController.class).getAllHateoas()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @PostMapping("/hateoas")
    public ResponseEntity<EntityModel<Envios>> addHateoas(@RequestBody Envios envio) {
        Envios nuevo = enviosService.add(envio);

        EntityModel<Envios> model = EntityModel.of(nuevo,
            linkTo(methodOn(EnviosController.class).getByIdHateoas(nuevo.getId())).withSelfRel(),
            linkTo(methodOn(EnviosController.class).getAllHateoas()).withRel("todos-los-envios")
        );

        return ResponseEntity
            .created(linkTo(methodOn(EnviosController.class).getByIdHateoas(nuevo.getId())).toUri())
            .body(model);
    }

    @PutMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Envios>> updateHateoas(@PathVariable Integer id, @RequestBody Envios envio) {
        Envios actualizado = enviosService.update(id, envio);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        EntityModel<Envios> model = EntityModel.of(actualizado,
            linkTo(methodOn(EnviosController.class).getByIdHateoas(id)).withSelfRel(),
            linkTo(methodOn(EnviosController.class).getAllHateoas()).withRel("todos-los-envios")
        );

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<String>> deleteHateoas(@PathVariable Integer id) {
        Envios eliminado = enviosService.delete(id);
        if (eliminado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EntityModel.of("Envio no encontrado"));
        }

        EntityModel<String> respuesta = EntityModel.of("Envío eliminado correctamente.",
            linkTo(methodOn(EnviosController.class).getAllHateoas()).withRel("todos-los-envios"),
            linkTo(methodOn(EnviosController.class).addHateoas(null)).withRel("crear-nuevo-envio")
        );

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/hateoas/nuevo-con-cliente/{idCliente}")
    public ResponseEntity<EntityModel<Envios>> crearEnvioConClienteHateoas(@PathVariable Integer idCliente, @RequestBody Envios envio) {
        try {
            Envios nuevo = enviosService.crearEnvioConCliente(idCliente, envio);

            EntityModel<Envios> model = EntityModel.of(nuevo,
                linkTo(methodOn(EnviosController.class).getByIdHateoas(nuevo.getId())).withSelfRel(),
                linkTo(methodOn(EnviosController.class).getAllHateoas()).withRel("todos-los-envios")
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(model);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
