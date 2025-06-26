package com.clientes_api.controller;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.models.ApiResponse;
import com.clientes_api.models.Cliente;
import com.clientes_api.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;



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
            return ResponseEntity .status(HttpStatus.CREATED).body(new ApiResponse(201, "Se ha creado el cliente."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, ex.getMessage()));

    }


    }
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    @PutMapping("/{idCliente}")
    public ResponseEntity<?> actualizarClienteId(@PathVariable Integer idCliente,Cliente cliente){
        try {
            Cliente clienteActualizado = clienteService.actualizarCliente(idCliente,cliente);
            return ResponseEntity .status(HttpStatus.OK).body(new ApiResponse(201, "Se ha actualizado el cliente."));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(400, ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, ex.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/{idCliente}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Integer idCliente){
        Cliente cliente = clienteService.getById(idCliente);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/preferencias")
    public void clientePreferencia(@PathVariable Integer idCliente){
        return;
    }

    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    @GetMapping("/hateoas/{idCliente}")
    public ResponseEntity<EntityModel<Cliente>> getClienteByIdHateoas(@PathVariable Integer idCliente) {
    Cliente cliente = clienteService.getById(idCliente);

    EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
        linkTo(methodOn(ClienteController.class).getClienteByIdHateoas(idCliente)).withSelfRel(),
        linkTo(methodOn(ClienteController.class).getAllHateoas()).withRel("todos-los-clientes"),
        linkTo(methodOn(ClienteController.class).actualizarClienteId(idCliente, null)).withRel("actualizar-cliente")
    );

    return ResponseEntity.ok(clienteModel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/hateoas/lista")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> getAllHateoas() {
    List<Cliente> clientes = clienteService.getAll();

    List<EntityModel<Cliente>> clientesModel = new ArrayList<>();

    for (Cliente cliente : clientes) {
        EntityModel<Cliente> model = EntityModel.of(cliente,
            linkTo(methodOn(ClienteController.class).getClienteByIdHateoas(cliente.getId() != null ? cliente.getId().intValue() : null)).withSelfRel()
        );
        clientesModel.add(model);
    }

    CollectionModel<EntityModel<Cliente>> collectionModel = CollectionModel.of(clientesModel,
        linkTo(methodOn(ClienteController.class).getAllHateoas()).withSelfRel()
    );

    return ResponseEntity.ok(collectionModel);
    }

}
