package com.perfulandia.ventas_api.controller;

import com.perfulandia.ventas_api.client.ClienteRestClient;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venta")
public class VentaController {
    @Autowired
    private VentaService ventaService;

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
        Venta nuevaVenta = ventaService.save(venta);
        return ResponseEntity.ok(nuevaVenta);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Venta>> listaVentasByIdCliente(@PathVariable Long idCliente){
        return ResponseEntity.ok(ventaService.getVentasByIdCliente(idCliente));
    }

}
