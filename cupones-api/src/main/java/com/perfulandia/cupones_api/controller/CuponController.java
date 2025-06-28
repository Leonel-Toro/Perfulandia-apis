    package com.perfulandia.cupones_api.controller;

    import java.util.List;

    import org.springframework.beans.factory.annotation.Autowired;
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
    import org.springframework.http.ResponseEntity;
    import java.util.stream.Collectors;
    import java.util.List;


    import com.perfulandia.cupones_api.dto.CuponDTO;
    import com.perfulandia.cupones_api.models.Cupon;
    import com.perfulandia.cupones_api.services.CuponServices;

    @RestController
    @RequestMapping("/api/cupones")
    public class CuponController {

        @Autowired
        private CuponServices cuponServices;

        
        @GetMapping
        public List<Cupon> getAll() {
            return cuponServices.getAll();
        }

        @GetMapping("/{id}")
        public Cupon getById(@PathVariable Long id) {
            return cuponServices.getById(id);
        }

        @PostMapping("/validar")
        public Cupon validarCupon(@RequestBody CuponDTO request) {
            return cuponServices.validarCupon(request);
        }

        @PostMapping
        public Cupon save(@RequestBody Cupon cupon) {
            return cuponServices.save(cupon);
        }

        @PutMapping("/{id}")
        public Cupon update(@PathVariable Long id, @RequestBody Cupon cupon) {
            return cuponServices.update(id, cupon);
        }

        @DeleteMapping("/{id}")
        public Cupon delete(@PathVariable Long id) {
            return cuponServices.delete(id);
        }

        // MÉTODOS HATEOAS

        @GetMapping("/hateoas/{id}")
        public ResponseEntity<EntityModel<Cupon>> getByIdHateoas(@PathVariable Long id) {
        Cupon cupon = cuponServices.getById(id);

        EntityModel<Cupon> cuponModel = EntityModel.of(cupon,
            linkTo(methodOn(CuponController.class).getByIdHateoas(id)).withSelfRel(),
            linkTo(methodOn(CuponController.class).getAllHateoas()).withRel("todos-los-cupones"),
            linkTo(methodOn(CuponController.class).updateHateoas(id, null)).withRel("actualizar"),
            linkTo(methodOn(CuponController.class).delete(id)).withRel("eliminar")
        );

        return ResponseEntity.ok(cuponModel);
        }

        @GetMapping("/hateoas")
        public ResponseEntity<CollectionModel<EntityModel<Cupon>>> getAllHateoas() {
        List<EntityModel<Cupon>> cuponesModel = cuponServices.getAll().stream()
            .map(cupon -> EntityModel.of(cupon,
                linkTo(methodOn(CuponController.class).getByIdHateoas(cupon.getId())).withSelfRel()
            ))
            .collect(Collectors.toList());

        CollectionModel<EntityModel<Cupon>> collectionModel = CollectionModel.of(cuponesModel,
            linkTo(methodOn(CuponController.class).getAllHateoas()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
        }

        @PostMapping("/hateoas")
        public ResponseEntity<EntityModel<Cupon>> saveHateoas(@RequestBody Cupon cupon) {
        Cupon nuevo = cuponServices.save(cupon);

        EntityModel<Cupon> cuponModel = EntityModel.of(nuevo,
            linkTo(methodOn(CuponController.class).getByIdHateoas(nuevo.getId())).withSelfRel(),
            linkTo(methodOn(CuponController.class).getAllHateoas()).withRel("todos-los-cupones")
        );

        return ResponseEntity
            .created(linkTo(methodOn(CuponController.class).getByIdHateoas(nuevo.getId())).toUri())
            .body(cuponModel);
        }

    @PutMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Cupon>> updateHateoas(@PathVariable Long id, @RequestBody Cupon cupon) {
        Cupon actualizado = cuponServices.update(id, cupon);

        EntityModel<Cupon> cuponModel = EntityModel.of(actualizado,
            linkTo(methodOn(CuponController.class).getByIdHateoas(id)).withSelfRel(),
            linkTo(methodOn(CuponController.class).getAllHateoas()).withRel("todos-los-cupones")
        );

        return ResponseEntity.ok(cuponModel);
    }

    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<String>> deleteHateoas(@PathVariable Long id) {
    cuponServices.delete(id);

    EntityModel<String> respuesta = EntityModel.of("Cupón eliminado correctamente.",
        linkTo(methodOn(CuponController.class).getAllHateoas()).withRel("todos-los-cupones"),
        linkTo(methodOn(CuponController.class).saveHateoas(null)).withRel("crear-nuevo-cupon")
    );

    return ResponseEntity.ok(respuesta);
    }

}


