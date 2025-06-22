package com.perfulandia.ventas_api.controller;

import com.clientes_api.models.ApiResponse;
import com.clientes_api.models.Cliente;
import com.perfulandia.ventas_api.dto.RegistroVendedorDTO;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.service.VendedorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/vendedor")
public class VendedorController {
    @Autowired
    private VendedorService vendedorService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<Vendedor>> getAll(){
        return ResponseEntity.ok(vendedorService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{idVendedor}")
    public ResponseEntity<Vendedor> getVendedorById(@PathVariable Long idVendedor){
        Vendedor vendedor = vendedorService.getById(idVendedor);
        return ResponseEntity.ok(vendedor);
    }
    
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevoVendedor(@RequestBody RegistroVendedorDTO request, HttpServletRequest httpRequest){
        List<String> errores = new ArrayList<>();
        if(request.getSucursal() == null || request.getSucursal().equals("")){
            errores.add("Sucursal: La sucursal no debe estar vacia.");
        }

        if(request.getMetaMensual() == null || request.getMetaMensual().compareTo(BigDecimal.ZERO) <= 0){
            errores.add("Meta Mensual: La meta mensual no debe estar vacia o debe ser mayor a 0");
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, String.join("; ", errores)));
        }
        try{
            vendedorService.registrarVendedor(request);
        }catch (HttpClientErrorException | HttpServerErrorException ex){
            return ResponseEntity.badRequest().body(new ApiResponse(400, ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{idVendedor}")
    public ResponseEntity<?> actualizarVendedor(@PathVariable Long idVendedor, Vendedor vendedor){
        try {
            Vendedor vendedorActualizado = vendedorService.actualizarVendedor(idVendedor,vendedor);
            return ResponseEntity .status(HttpStatus.OK).body(new ApiResponse(201, "Se ha actualizado el vendedor."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, ex.getMessage()));
        }
    }
}
