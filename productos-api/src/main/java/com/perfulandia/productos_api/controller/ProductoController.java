package com.perfulandia.productos_api.controller;

import com.perfulandia.productos_api.models.ApiResponse;
import com.perfulandia.productos_api.models.Producto;
import com.perfulandia.productos_api.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @PreAuthorize("hasRole('ADMIN','VENDEDOR')")
    @GetMapping("/")
    public ResponseEntity<?> listaProductos(){
        return ResponseEntity.status(HttpStatus.OK).body(productoService.verProductos());
    }
    @PreAuthorize("hasRole('ADMIN','VENDEDOR')")
    @PostMapping("/")
    public ResponseEntity<?> ingresarNuevoProducto(@RequestBody Producto nuevoProducto){
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

    @PreAuthorize("hasRole('ADMIN','VENDEDOR')")
    @GetMapping("/{idProducto}")
    public ResponseEntity<?> verProducto(@PathVariable Long idProducto){
        if(productoService.productoPorId(idProducto) != null){
            return ResponseEntity.status(HttpStatus.FOUND).body(productoService.productoPorId(idProducto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(400, "No existe ese producto."));
    }

    @PreAuthorize("hasRole('ADMIN','VENDEDOR')")
    @GetMapping("/categoria")
    public ResponseEntity<?> productosPorNombreCategoria(@RequestParam String nombreCategoria){
        List<Producto> productos = productoService.listaProductosByCategoria(nombreCategoria);
        if(productos!=null){
            return ResponseEntity.status(HttpStatus.OK).body(productos);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(400,"No se han encontrado productos con esa categoria."));
    }

}
