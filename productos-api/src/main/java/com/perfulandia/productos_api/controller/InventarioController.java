package com.perfulandia.productos_api.controller;

import com.perfulandia.productos_api.dto.InventarioDTO;
import com.perfulandia.productos_api.models.ApiResponse;
import com.perfulandia.productos_api.models.Inventario;
import com.perfulandia.productos_api.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
    @Autowired
    private InventarioService inventarioService;

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/lista")
    public ResponseEntity<?> listaInventario(){
        return ResponseEntity.status(HttpStatus.OK).body(inventarioService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @PostMapping("/nuevo")
    public ResponseEntity<?> guardarInventario(@RequestBody Inventario inventario) {
        try {
            Inventario guardado = inventarioService.guardarInventario(inventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @PutMapping("/ajustar")
    public ResponseEntity<?> ajusteInventario(@RequestBody InventarioDTO request) {
        Inventario inventario = inventarioService.findByIdProducto(request.getIdProducto());
        if(inventario == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(400,"Producto no existe."));
        }

        if (inventario.getCantidad() < request.getCantidad()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400,"Stock insufuciente para el producto "+ inventario.getProducto().getNombreProducto()));
        }

        inventario.setCantidad(inventario.getCantidad() - request.getCantidad());
        inventarioService.guardarInventario(inventario);

        return ResponseEntity.ok().body(new ApiResponse(200, "Stock actualizado correctamente"));
    }

    // Metodos HATEOAS
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/lista")
    public ResponseEntity<CollectionModel<EntityModel<Inventario>>> listaInventarioHateoas() {
    List<EntityModel<Inventario>> inventarios = inventarioService.findAll().stream()
        .map(inv -> {
            EntityModel<Inventario> model = EntityModel.of(inv);
            model.add(linkTo(methodOn(ProductoController.class).verProducto(inv.getProducto().getId())).withRel("ver-producto"));
            model.add(linkTo(methodOn(InventarioController.class).ajusteInventarioHateoas(new InventarioDTO(inv.getProducto().getId(), 1))).withRel("ajustar-inventario"));
            return model;
        })
        .collect(Collectors.toList());

    return ResponseEntity.ok(CollectionModel.of(inventarios,
        linkTo(methodOn(InventarioController.class).listaInventarioHateoas()).withSelfRel()
    ));
    }   

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @PutMapping("/hateoas/ajustar")
    public ResponseEntity<EntityModel<ApiResponse>> ajusteInventarioHateoas(@RequestBody InventarioDTO request) {
    Inventario inventario = inventarioService.findByIdProducto(request.getIdProducto());
    if (inventario == null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(EntityModel.of(new ApiResponse(400, "Producto no existe.")));
    if (inventario.getCantidad() < request.getCantidad()) {
        return ResponseEntity.badRequest()
            .body(EntityModel.of(new ApiResponse(400, "Stock insuficiente para el producto " + inventario.getProducto().getNombreProducto())));
    }

    inventario.setCantidad(inventario.getCantidad() - request.getCantidad());
    inventarioService.guardarInventario(inventario);

    EntityModel<ApiResponse> model = EntityModel.of(new ApiResponse(200, "Stock actualizado correctamente"),
        linkTo(methodOn(InventarioController.class).listaInventarioHateoas()).withRel("ver-inventario"),
        linkTo(methodOn(ProductoController.class).verProducto(inventario.getProducto().getId())).withRel("ver-producto")
    );

    return ResponseEntity.ok(model);
    }
}
