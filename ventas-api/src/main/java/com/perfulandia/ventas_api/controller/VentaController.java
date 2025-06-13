package com.perfulandia.ventas_api.controller;

import com.clientes_api.models.ApiResponse;
import com.perfulandia.ventas_api.models.DetalleVenta;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.service.VendedorService;
import com.perfulandia.ventas_api.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/venta")
@RequiredArgsConstructor
public class VentaController {
    @Autowired
    private VentaService ventaService;
    @Autowired
    private VendedorService vendedorService;

    @GetMapping("")
    public ResponseEntity<List<Venta>> listaVentas(){
        List<Venta> listaVentas = ventaService.getVentas();
        if(listaVentas.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(listaVentas);
    }

    @PostMapping("/nueva")
    public ResponseEntity<?> nuevaVenta(@RequestBody Venta venta, DetalleVenta detalleVenta){
        try{
            Venta nuevaVenta = ventaService.procesarVenta(venta,detalleVenta);
            return ResponseEntity.ok(venta);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(500, "Error interno: " + e.getMessage()));
        }
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Venta>> listaVentasByIdCliente(@PathVariable Long idCliente){
        List<Venta> ventas = ventaService.getVentasByIdCliente(idCliente);
        return ResponseEntity.ok(ventas);
    }

   
    @GetMapping("/vendedor/{idVendedor}")
    public ResponseEntity<?> listarVentasByVendedor(@PathVariable Long idVendedor) {
        try {
            Vendedor vendedor = vendedorService.getById(idVendedor);
            if(vendedor == null){
                return ResponseEntity.badRequest().body(new ApiResponse(400, "Vendedor no encontrado."));
            }

            List<Venta> ventas = ventaService.getVentasByIdVendedor(vendedor);
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Error al obtener ventas: " + e.getMessage()));
        }
    }

}
