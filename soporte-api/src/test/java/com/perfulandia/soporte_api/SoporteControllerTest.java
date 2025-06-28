package com.perfulandia.soporte_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.perfulandia.soporte_api.controllers.TicketController;
import com.perfulandia.soporte_api.services.TicketService;
import com.perfulandia.soporte_api.Model.Ticket;

class SoporteControllerTest {

    @Test
    void testCreacionTicket_Cumplido() {
    // Arrange
    TicketController controller = new TicketController();
    Ticket ticket = new Ticket();
    ticket.setAsunto("Prueba de ticket");
    ticket.setMensaje("Esto es un ticket de prueba");
    ticket.setEstado("Abierto");
    ticket.setFechaCreacion(new java.sql.Date(new java.util.Date().getTime()));
    // Agrega más propiedades si son requeridas por el servicio

    Ticket ticketGuardado = new Ticket();
    ticketGuardado.setId(1);
    ticketGuardado.setAsunto(ticket.getAsunto());
    ticketGuardado.setMensaje(ticket.getMensaje());
    ticketGuardado.setEstado(ticket.getEstado());
    ticketGuardado.setFechaCreacion(ticket.getFechaCreacion());

    TicketService mockService = Mockito.mock(TicketService.class);
    Mockito.when(mockService.add(Mockito.any(Ticket.class))).thenReturn(ticketGuardado);

    ReflectionTestUtils.setField(controller, "ticketService", mockService);

    // Act
    ResponseEntity<?> response = controller.creacionTicket(ticket);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody() instanceof Ticket);
    assertEquals("Prueba de ticket", ((Ticket) response.getBody()).getAsunto());
}

    @Test
    void testCreacionTicket_Error() {
        // Arrange
        TicketController controller = new TicketController();
        Ticket ticket = new Ticket();
        ticket.setAsunto("Prueba de ticket");
        ticket.setMensaje("Esto es un ticket de prueba");
        ticket.setEstado("Abierto");
        ticket.setFechaCreacion(new java.sql.Date(new java.util.Date().getTime()));

        TicketService mockService = Mockito.mock(TicketService.class);
        Mockito.when(mockService.add(Mockito.any(Ticket.class))).thenThrow(new RuntimeException("Error al crear el ticket"));

        ReflectionTestUtils.setField(controller, "ticketService", mockService);

        // Act
        ResponseEntity<?> response = controller.creacionTicket(ticket);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testTicketNuevo_ErrorInterno() {
        // Arrange
        TicketController controller = new TicketController();
        Ticket ticket = new Ticket();
        ticket.setAsunto("Prueba de ticket");
        ticket.setMensaje("Esto es un ticket de prueba");
        ticket.setEstado("Abierto");
        ticket.setFechaCreacion(new java.sql.Date(new java.util.Date().getTime()));

        TicketService mockService = Mockito.mock(TicketService.class);
        Mockito.when(mockService.add(Mockito.any(Ticket.class))).thenThrow(new RuntimeException("Error interno"));

        ReflectionTestUtils.setField(controller, "ticketService", mockService);

        // Act
        ResponseEntity<?> response = controller.creacionTicket(ticket);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }
    

    
}
