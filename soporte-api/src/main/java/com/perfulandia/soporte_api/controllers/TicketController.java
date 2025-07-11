package com.perfulandia.soporte_api.controllers;

import com.perfulandia.soporte_api.Model.Ticket;
import com.perfulandia.soporte_api.services.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/soporte")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // ✅ Normal: obtener todos los tickets
    @GetMapping({"","/"})
    public ResponseEntity<List<Ticket>> getAll() {
        return ResponseEntity.ok(ticketService.getAll());
    }

    // ✅ Normal: obtener uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Ticket ticket = ticketService.getById(id);
        return ticket != null ? ResponseEntity.ok(ticket) : ResponseEntity.notFound().build();
    }

    // ✅ Normal: crear ticket
    @PostMapping({"", "/"})
    public ResponseEntity<?> creacionTicket(@RequestBody Ticket ticket) {
        try {
            Ticket nuevoTicket = ticketService.add(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTicket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el ticket: " + e.getMessage());
        }
    }

    // ✅ Normal: actualizar
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Ticket ticket) {
        Ticket actualizado = ticketService.update(id, ticket);
        return actualizado != null ? ResponseEntity.ok(actualizado)
                                   : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket no encontrado");
    }

    // ✅ Normal: eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Ticket eliminado = ticketService.delete(id);
        return eliminado != null ? ResponseEntity.ok(eliminado)
                                 : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket no encontrado");
    }

    // ✅ HATEOAS: lista completa
    @GetMapping("/hateoas/")
    public ResponseEntity<CollectionModel<EntityModel<Ticket>>> getAllHateoas() {
        List<EntityModel<Ticket>> ticketsHateoas = ticketService.getAll().stream()
            .map(t -> EntityModel.of(
                t,
                linkTo(methodOn(TicketController.class).getByIdHateoas(t.getId())).withRel("ticket-por-id")
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(
                ticketsHateoas,
                linkTo(methodOn(TicketController.class).getAllHateoas()).withRel("lista-tickets")
            )
        );
    }

    // ✅ HATEOAS: uno con links completos
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<?> getByIdHateoas(@PathVariable Integer id) {
        Ticket ticket = ticketService.getById(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Ticket> ticketModel = EntityModel.of(
            ticket,
            linkTo(methodOn(TicketController.class).getByIdHateoas(id)).withRel("ticket-por-id"),
            linkTo(methodOn(TicketController.class).getAllHateoas()).withRel("lista-tickets"),
            linkTo(methodOn(TicketController.class).updateHateoas(id, null)).withRel("actualizar-ticket"),
            linkTo(methodOn(TicketController.class).deleteHateoas(id)).withRel("eliminar-ticket")
        );

        return ResponseEntity.ok(ticketModel);
    }

    // ✅ HATEOAS: crear
    @PostMapping("/hateoas")
    public ResponseEntity<?> creacionTicketHateoas(@RequestBody Ticket ticket) {
        try {
            Ticket nuevoTicket = ticketService.add(ticket);

            EntityModel<Ticket> ticketModel = EntityModel.of(
                nuevoTicket,
                linkTo(methodOn(TicketController.class).getByIdHateoas(nuevoTicket.getId())).withRel("ticket-por-id"),
                linkTo(methodOn(TicketController.class).getAllHateoas()).withRel("lista-tickets")
            );

            return ResponseEntity
                .created(linkTo(methodOn(TicketController.class).getByIdHateoas(nuevoTicket.getId())).toUri())
                .body(ticketModel);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el ticket: " + e.getMessage());
        }
    }

    // ✅ HATEOAS: actualizar
    @PutMapping("/hateoas/{id}")
    public ResponseEntity<?> updateHateoas(@PathVariable Integer id, @RequestBody Ticket ticket) {
        Ticket actualizado = ticketService.update(id, ticket);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket no encontrado");
        }

        EntityModel<Ticket> ticketModel = EntityModel.of(
            actualizado,
            linkTo(methodOn(TicketController.class).getByIdHateoas(id)).withRel("ticket-por-id"),
            linkTo(methodOn(TicketController.class).getAllHateoas()).withRel("lista-tickets")
        );

        return ResponseEntity.ok(ticketModel);
    }

    // ✅ HATEOAS: eliminar
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> deleteHateoas(@PathVariable Integer id) {
        Ticket eliminado = ticketService.delete(id);
        if (eliminado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket no encontrado");
        }

        EntityModel<String> respuesta = EntityModel.of(
            "Ticket eliminado correctamente",
            linkTo(methodOn(TicketController.class).getAllHateoas()).withRel("lista-tickets"),
            linkTo(methodOn(TicketController.class).creacionTicketHateoas(null)).withRel("crear-ticket")
        );

        return ResponseEntity.ok(respuesta);
    }
}
