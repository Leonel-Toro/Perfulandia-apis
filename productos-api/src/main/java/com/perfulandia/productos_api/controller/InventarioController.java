package com.perfulandia.productos_api.controller;

import com.perfulandia.productos_api.dto.InventarioDTO;
import com.perfulandia.productos_api.models.ApiResponse;
import com.perfulandia.productos_api.models.Inventario;
import com.perfulandia.productos_api.models.Producto;
import com.perfulandia.productos_api.service.InventarioService;
import com.perfulandia.productos_api.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {
    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/lista")
    public ResponseEntity<?> listaInventario(){
        return ResponseEntity.status(HttpStatus.OK).body(inventarioService.findAll());
    }

    @PostMapping("/nuevo")
    public ResponseEntity<?> guardarInventario(@RequestBody Inventario inventario) {
        try {
            Inventario guardado = inventarioService.guardarInventario(inventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage()));
        }
    }

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
}
