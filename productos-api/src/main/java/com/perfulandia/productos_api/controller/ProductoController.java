package com.perfulandia.productos_api.controller;

import com.perfulandia.productos_api.dto.ProductoInventarioDTO;
import com.perfulandia.productos_api.models.ApiResponse;
import com.perfulandia.productos_api.models.Producto;
import com.perfulandia.productos_api.service.ProductoService;
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
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/listaProductos")
    public ResponseEntity<?> listaProductos(){
        return ResponseEntity.status(HttpStatus.OK).body(productoService.verProductos());
    }
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @PostMapping("/nuevoProducto")
    public ResponseEntity<?> ingresarNuevoProducto(@RequestBody ProductoInventarioDTO nuevoProducto){
        try{
            Producto producto = productoService.nuevoProducto(nuevoProducto);
            if(producto != null){
                    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(201,"Se ha ingresado un nuevo producto."));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(400,"Ha ocurrido un error con el producto."));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(400,ex.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/{idProducto}")
    public ResponseEntity<?> verProducto(@PathVariable Long idProducto){
        if(productoService.productoPorId(idProducto) != null){
            return ResponseEntity.status(HttpStatus.OK).body(productoService.productoPorId(idProducto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(400, "No existe ese producto."));
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/categoria")
    public ResponseEntity<?> productosPorNombreCategoria(@RequestParam Long idCategoria){
        List<Producto> productos = productoService.listaProductosByCategoria(idCategoria);
        if(productos!=null){
            return ResponseEntity.status(HttpStatus.OK).body(productos);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(400,"No se han encontrado productos con esa categoria."));
    }

    //Metodos HATEOAS
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/listaProductos")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listaProductosHateoas() {
        List<EntityModel<Producto>> productos = productoService.verProductos().stream()
        .map(prod -> (EntityModel<Producto>) EntityModel.of(prod,
            linkTo(methodOn(ProductoController.class).verProducto(prod.getId())).withRel("producto-por-id"),
            linkTo(methodOn(ProductoController.class).productosPorNombreCategoria(prod.getCategoria().getId())).withRel("productos-de-la-misma-categoria")
        ))
        .collect(Collectors.toList());

    return ResponseEntity.ok(CollectionModel.of(productos,
        linkTo(methodOn(ProductoController.class).listaProductosHateoas()).withRel("lista-productos")
    ));
}

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/{idProducto}")
    public ResponseEntity<EntityModel<Producto>> verProductoHateoas(@PathVariable Long idProducto) {
    Producto producto = productoService.productoPorId(idProducto);
    if (producto == null)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    EntityModel<Producto> model = EntityModel.of(producto,
        linkTo(methodOn(ProductoController.class).verProductoHateoas(idProducto)).withRel("producto-por-id"),
        linkTo(methodOn(ProductoController.class).listaProductosHateoas()).withRel("lista-productos")
    );

    return ResponseEntity.ok(model);
    }


}
