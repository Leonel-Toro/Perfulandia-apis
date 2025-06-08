package com.clientes_api.controller;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.models.ApiResponse;
import com.clientes_api.models.Cliente;
import com.clientes_api.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/lista")
    public ResponseEntity<List<Cliente>> getAll() {
        return ResponseEntity.ok(clienteService.getAll());
    }

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevoCliente(@RequestBody RegistroClienteDTO request){
        try {
            clienteService.registrarCliente(request);
            return ResponseEntity.ok(new ApiResponse(201, "Se ha creado el cliente."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, ex.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Integer idCliente){
        Cliente cliente = clienteService.getById(idCliente);
        return ResponseEntity.ok(cliente);
    }
}
