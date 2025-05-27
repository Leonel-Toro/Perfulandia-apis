package com.perfulandia.ventas_api.controller;

import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.service.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendedor")
public class VendedorController {
    @Autowired
    private VendedorService vendedorService;

    @GetMapping("")
    public ResponseEntity<List<Vendedor>> getAll(){
        return ResponseEntity.ok(vendedorService.getAll());
    }
    
    @GetMapping("/{idVendedor}")
    public ResponseEntity<Vendedor> getVendedorById(@PathVariable Long idVendedor){
        Vendedor vendedor = vendedorService.getById(idVendedor);
        return ResponseEntity.ok(vendedor);
    }
    
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevoVendedor(@RequestBody Vendedor vendedor){
        Map<String,String> errores = new LinkedHashMap<>();
        if(vendedor.getSucursal() == null || vendedor.getSucursal().equals("")){
            errores.put("sucursal","La sucursal no debe estar vacia.");
        }

        if(vendedor.getMetaMensual() == null || vendedor.getMetaMensual().compareTo(BigDecimal.ZERO) <= 0){
            errores.put("meta mensual","La meta mensual no debe estar vacia o debe ser mayor a 0");
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(errores);
        }

        Vendedor nuevoVendedor = vendedorService.save(vendedor);
        return ResponseEntity.status(HttpStatus.OK).body(nuevoVendedor);
    }
}
