package com.perfulandia.ventas_api.controller;

import com.clientes_api.models.ApiResponse;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.service.VendedorService;
import com.perfulandia.ventas_api.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/venta")
public class VentaController {
    @Autowired
    private VentaService ventaService;
    @Autowired
    private VendedorService vendedorService;

    @GetMapping("")
    public ResponseEntity<?> listaVentas(){
        List<Venta> listaVentas = ventaService.getVentas();
        if(listaVentas== null) {
            return ResponseEntity.badRequest().body("No se han encontrado ventas");
        }
        return ResponseEntity.ok(listaVentas);
    }

    @PostMapping("/nueva")
    public ResponseEntity<?> nuevaVenta(@RequestBody Venta venta){
        try{
            Venta nuevaVenta = ventaService.procesarVenta(venta);
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
        return ResponseEntity.ok(ventaService.getVentasByIdCliente(idCliente));
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
