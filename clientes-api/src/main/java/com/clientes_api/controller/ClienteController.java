package com.clientes_api.controller;

import com.clientes_api.models.Cliente;
import com.clientes_api.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/lista")
    public ResponseEntity<List<Cliente>> getAll() {
        return ResponseEntity.ok(clienteService.getAll());
    }

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevoCliente(@RequestBody Cliente cliente){
        Map<String,String> errores = new LinkedHashMap<>();
        if(cliente.getNombre() == null || cliente.getNombre().equals("")){
            errores.put("nombre","El nombre no debe estar vacio.");
        }

        if(cliente.getApellido() == null || cliente.getApellido().equals("")){
            errores.put("apellido","El apellido no debe estar vacio");
        }

        if(cliente.getTelefono() == null || cliente.getTelefono().matches("^\\d{9}$")){
            errores.put("telefono", "El teléfono debe contener 9 dígitos");
        }

        if(cliente.getRut() == null){
            errores.put("rut", "El RUT no es válido");
        }

        if(cliente.getDireccion() == null || cliente.getDireccion().equals("")){
            errores.put("direccion","La dirección no debe estar vacia.");
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(errores);
        }

        Cliente nuevoCliente = clienteService.save(cliente);
        return ResponseEntity.status(HttpStatus.OK).body(nuevoCliente);
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Integer idCliente){
        Cliente cliente = clienteService.getById(idCliente);
        return ResponseEntity.ok(cliente);
    }
}
