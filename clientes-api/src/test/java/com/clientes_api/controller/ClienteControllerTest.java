package com.clientes_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.services.ClienteService;
import com.clientes_api.models.ApiResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ClienteControllerTest {

    @Test
    void testNuevoCliente_Cumplido() {
    // Arrange
    ClienteController controller = new ClienteController();
    RegistroClienteDTO dto = new RegistroClienteDTO();
    // ponemos propiedades en dto según sea necesario para una solicitud válida

    ClienteService mockService = org.mockito.Mockito.mock(ClienteService.class);
    org.springframework.test.util.ReflectionTestUtils.setField(controller, "clienteService", mockService);

    // Act
    ResponseEntity<?> response = controller.nuevoCliente(dto);

    // Assert
    Object responseBody = response.getBody();
    org.junit.jupiter.api.Assertions.assertNotNull(responseBody, "La nueva respuesta no debe ser nula");
    org.junit.jupiter.api.Assertions.assertEquals(201, ((com.clientes_api.models.ApiResponse)responseBody).getStatus());
    org.junit.jupiter.api.Assertions.assertEquals("Se ha creado el cliente.", ((com.clientes_api.models.ApiResponse)responseBody).getMessage());
    }

    @Test
    void testNuevoCliente_Fallo() {
    // Arrange
    // Aquí se simula un fallo de validación, por ejemplo, si el DTO no cumple con las restricciones
    ClienteController controller = new ClienteController();
    RegistroClienteDTO dto = new RegistroClienteDTO();
    // ponemos propiedades en dto que causen un fallo de validación
    ClienteService mockService = org.mockito.Mockito.mock(ClienteService.class);
    org.springframework.test.util.ReflectionTestUtils.setField(controller, "clienteService", mockService);

    // Act
    ResponseEntity<?> response = controller.nuevoCliente(dto);
    // Assert
    Object responseBody = response.getBody();
    org.junit.jupiter.api.Assertions.assertNotNull(responseBody, "La respuesta no debe ser nula");
    org.junit.jupiter.api.Assertions.assertEquals(400, ((com.clientes_api.models.ApiResponse)responseBody).getStatus());
    org.junit.jupiter.api.Assertions.assertEquals("Error al crear el cliente: [Lista de errores de validación]", ((com.clientes_api.models.ApiResponse)responseBody).getMessage());
    }

    


}
