package com.perfulandia.soporte_api.controllers;
import com.perfulandia.soporte_api.Model.Ticket;
import com.perfulandia.soporte_api.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.stream.Collectors;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/soporte")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Creamos una lista de tickets para simular una base de datos
    private List<Ticket> tickets = new ArrayList<>();
    public TicketController() {
        tickets.add(new Ticket(1, "Problema de red", "No tengo conexión a Internet", "Abierto", null));
        tickets.add(new Ticket(2, "Error de software", "La aplicación se cierra inesperadamente", "Cerrado", null));
        tickets.add(new Ticket(3, "Consulta de cuenta", "¿Cómo restablezco mi contraseña?", "En progreso", null));
    }

    // Obtenemos todos los tickets (GET)
    @GetMapping({"","/"})
    public ResponseEntity<List<Ticket>> getAll() {
        return ResponseEntity.ok(ticketService.getAll());
    }

    // Obtenemos un ticket por su ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Ticket ticket = ticketService.getById(id);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear un nuevo ticket (POST)
    @PostMapping({"", "/"})
    public ResponseEntity<?> creacionTicket(@RequestBody Ticket ticket) {
        try {
            Ticket nuevoTicket = ticketService.add(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTicket);
        } catch (Exception e) {
            // Si quieres controlar errores también
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el ticket: " + e.getMessage());
        }
    }

    // Actualizar un ticket existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Ticket ticket) {
        Ticket actualizado = ticketService.update(id, ticket);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket no encontrado");
        }
    }

    // Eliminar un ticket por su ID (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Ticket eliminado = ticketService.delete(id);
        if (eliminado != null) {
            return ResponseEntity.ok(eliminado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket no encontrado");
        }
    }

    //Metodos HATEOAS
    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<Ticket>>> getAllHateoas() {
    List<Ticket> lista = ticketService.getAll();
    
    List<EntityModel<Ticket>> tickets = lista.stream()
        .map(ticket -> EntityModel.of(ticket,
            linkTo(methodOn(TicketController.class).getByIdHateoas(ticket.getId())).withSelfRel(),
            linkTo(methodOn(TicketController.class).deleteHateoas(ticket.getId())).withRel("eliminar"),
            linkTo(methodOn(TicketController.class).updateHateoas(ticket.getId(), null)).withRel("actualizar")
        ))
        .collect(Collectors.toList());

    return ResponseEntity.ok(
        CollectionModel.of(tickets,
            linkTo(methodOn(TicketController.class).getAllHateoas()).withSelfRel(),
            linkTo(methodOn(TicketController.class).creacionTicketHateoas(null)).withRel("crear-nuevo")
        )
    );
    }

    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Ticket>> getByIdHateoas(@PathVariable Integer id) {
    Ticket ticket = ticketService.getById(id);
    if (ticket == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    EntityModel<Ticket> model = EntityModel.of(ticket,
        linkTo(methodOn(TicketController.class).getByIdHateoas(id)).withSelfRel(),
        linkTo(methodOn(TicketController.class).getAllHateoas()).withRel("todos-los-tickets"),
        linkTo(methodOn(TicketController.class).updateHateoas(id, null)).withRel("actualizar"),
        linkTo(methodOn(TicketController.class).deleteHateoas(id)).withRel("eliminar")
    );

    return ResponseEntity.ok(model);
    }

    @PostMapping("/hateoas")
    public ResponseEntity<EntityModel<Ticket>> creacionTicketHateoas(@RequestBody Ticket ticket) {
    Ticket nuevo = ticketService.add(ticket);

    EntityModel<Ticket> model = EntityModel.of(nuevo,
        linkTo(methodOn(TicketController.class).getByIdHateoas(nuevo.getId())).withSelfRel(),
        linkTo(methodOn(TicketController.class).getAllHateoas()).withRel("todos-los-tickets")
    );

    return ResponseEntity
        .created(linkTo(methodOn(TicketController.class).getByIdHateoas(nuevo.getId())).toUri())
        .body(model);
    }


    @PutMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Ticket>> updateHateoas(@PathVariable Integer id, @RequestBody Ticket ticket) {
    Ticket actualizado = ticketService.update(id, ticket);
    if (actualizado == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    EntityModel<Ticket> model = EntityModel.of(actualizado,
        linkTo(methodOn(TicketController.class).getByIdHateoas(id)).withSelfRel(),
        linkTo(methodOn(TicketController.class).getAllHateoas()).withRel("todos-los-tickets")
    );

    return ResponseEntity.ok(model);
    }


    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<String>> deleteHateoas(@PathVariable Integer id) {
    Ticket eliminado = ticketService.delete(id);
    if (eliminado == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(EntityModel.of("Ticket no encontrado"));
    }

    EntityModel<String> model = EntityModel.of("Ticket eliminado correctamente",
        linkTo(methodOn(TicketController.class).getAllHateoas()).withRel("todos-los-tickets"),
        linkTo(methodOn(TicketController.class).creacionTicketHateoas(null)).withRel("crear-nuevo")
    );

    return ResponseEntity.ok(model);
    }



}
