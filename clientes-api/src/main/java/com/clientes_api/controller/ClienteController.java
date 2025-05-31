package com.clientes_api.controller;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.models.ApiResponse;
import com.clientes_api.models.Cliente;
import com.clientes_api.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> nuevoCliente(@RequestBody RegistroClienteDTO request){
        List<String> errores = new ArrayList<>();
        if(request.getNombre() == null || request.getNombre().equals("")){
            errores.add("Nombre: El nombre no debe estar vacio");
        }

        if(request.getApellido() == null || request.getApellido().equals("")){
            errores.add("Apellido: El apellido no debe estar vacio");
        }

        if(request.getTelefono() == null || !request.getTelefono().matches("^\\d{9}$")){
            errores.add("Telefono: El teléfono debe contener 9 dígitos");
        }

        if(request.getRut() == null){
            errores.add("Rut: El RUT no es válido");
        }

        if(request.getDireccion() == null || request.getDireccion().equals("")){
            errores.add("Direccion: La dirección no debe estar vacia");
        }

        if(request.getCorreo() == null || request.getCorreo().equals("")){
            errores.add("Correo: El correo es obligatorio");
        }

        if(request.getPassword() == null || request.getPassword().equals("") || request.getPassword().length() < 8){
            errores.add("Contraseña: La contraseña debe tener al menos 8 caracteres y no estar vacia");
        }

        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse(400,String.join("; ", errores)));
        }

        try{
            clienteService.registrarCliente(request);
        }catch (Exception error){
            return ResponseEntity.badRequest().body(new ApiResponse(400,error.getMessage()));
        }
        return ResponseEntity.ok().body(new ApiResponse(200,"Se ha creado el cliente."));
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Integer idCliente){
        Cliente cliente = clienteService.getById(idCliente);
        return ResponseEntity.ok(cliente);
    }
}
