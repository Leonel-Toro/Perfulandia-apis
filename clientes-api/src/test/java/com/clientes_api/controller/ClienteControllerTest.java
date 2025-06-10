package com.clientes_api.controller;

import org.springframework.http.ResponseEntity;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.services.ClienteService;

import org.junit.jupiter.api.Test;

class ClienteControllerTest {

    @Test
    void testNuevoCliente_Success() {
    // Arrange
    ClienteController controller = new ClienteController();
    RegistroClienteDTO dto = new RegistroClienteDTO();
    // Set properties on dto as needed for a valid request

    ClienteService mockService = org.mockito.Mockito.mock(ClienteService.class);
    org.springframework.test.util.ReflectionTestUtils.setField(controller, "clienteService", mockService);

    // Act
    ResponseEntity<?> response = controller.nuevoCliente(dto);

    // Assert
    org.junit.jupiter.api.Assertions.assertEquals(201, ((com.clientes_api.models.ApiResponse)response.getBody()).getStatus());
    org.junit.jupiter.api.Assertions.assertEquals("Se ha creado el cliente.", ((com.clientes_api.models.ApiResponse)response.getBody()).getMessage());
    }
}
