package com.perfulandia.ventas_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.service.VentaService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/venta")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @GetMapping("")
    public ResponseEntity<List<Venta>> listaVentas(){
        List<Venta> listaVentas = ventaService.getVentas();
        if(listaVentas.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(listaVentas);
    }

    @PostMapping("/nueva")
    public ResponseEntity<?> nuevaVenta(@RequestBody VentaConCuponDTO request){
        Venta ventaProcesada = ventaService.save(request.getVenta(), request.getCodigoCupon());
        if(ventaProcesada == null){
            return ResponseEntity.badRequest().body("Cliente o cupón inválido");
        }
        return ResponseEntity.ok(ventaProcesada);
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Venta>> listaVentasByIdCliente(@PathVariable Long idCliente){
        List<Venta> ventas = ventaService.getVentasByIdCliente(idCliente);
        return ResponseEntity.ok(ventas);
    }

    @Data
    public static class VentaConCuponDTO {
        private Venta venta;
        private String codigoCupon;
    }
}
