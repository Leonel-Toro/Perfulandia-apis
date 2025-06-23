package com.clientes_api.controller;
import org.springframework.http.ResponseEntity;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.services.ClienteService;

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
    ClienteController controller = new ClienteController();
    RegistroClienteDTO dto = new RegistroClienteDTO();
    // (Opcional) Configura dto con datos inválidos

    ClienteService mockService = Mockito.mock(ClienteService.class);
    // Aquí configuramos el mock para lanzar una excepción
    Mockito.doThrow(new IllegalArgumentException("Error de validación"))
           .when(mockService).registrarCliente(Mockito.any(RegistroClienteDTO.class));
    org.springframework.test.util.ReflectionTestUtils.setField(controller, "clienteService", mockService);

    // Act
    ResponseEntity<?> response = controller.nuevoCliente(dto);

    // Assert
    Object responseBody = response.getBody();
    assertNotNull(responseBody, "La respuesta no debe ser nula");
    assertEquals(400, ((com.clientes_api.models.ApiResponse)responseBody).getStatus());
    assertEquals("Error de validación", ((com.clientes_api.models.ApiResponse)responseBody).getMessage());
}

    @Test
    void testNuevoCliente_ErrorInterno() {
    // Arrange
    ClienteController controller = new ClienteController();
    RegistroClienteDTO dto = new RegistroClienteDTO();

    ClienteService mockService = Mockito.mock(ClienteService.class);
    // Simula un error inesperado del sistema
    Mockito.doThrow(new RuntimeException("Error inesperado del sistema"))
           .when(mockService).registrarCliente(Mockito.any(RegistroClienteDTO.class));
    org.springframework.test.util.ReflectionTestUtils.setField(controller, "clienteService", mockService);

    // Act
    ResponseEntity<?> response = controller.nuevoCliente(dto);

    // Assert
    Object responseBody = response.getBody();
    assertNotNull(responseBody, "La respuesta no debe ser nula");
    // Según tu controlador, esto devuelve 400, pero lo ideal sería 500.
    assertEquals(400, ((com.clientes_api.models.ApiResponse)responseBody).getStatus());
    assertEquals("Error inesperado del sistema", ((com.clientes_api.models.ApiResponse)responseBody).getMessage());
}

    
    


}
