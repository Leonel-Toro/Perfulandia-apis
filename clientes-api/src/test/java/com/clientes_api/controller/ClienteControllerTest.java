package com.clientes_api.controller;

import com.clientes_api.dto.RegistroClienteDTO;
import com.clientes_api.models.ApiResponse;
import com.clientes_api.models.Cliente;
import com.clientes_api.services.ClienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    private Cliente cliente1;

    @BeforeEach
    void setUp() {
        cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNombre("Gabriel");
        cliente1.setApellido("Astorga");
        cliente1.setRut("12345678-9");
        cliente1.setTelefono("123456789");
        cliente1.setDireccion("Calle Falsa 123");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllClientes() throws Exception {
        List<Cliente> clientes = Arrays.asList(cliente1);
        when(clienteService.getAll()).thenReturn(clientes);

        mockMvc.perform(get("/api/cliente/lista"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Gabriel"));
    }

    @Test
    public void testNuevoClienteSuccess() throws Exception {
        RegistroClienteDTO dto = new RegistroClienteDTO();
        dto.setNombre("Gabriel");
        dto.setApellido("Astorga");
        dto.setRut("12345678-9");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle Falsa 123");

        // Simulamos que no lanza excepción
        doNothing().when(clienteService).registrarCliente(any(RegistroClienteDTO.class));

        mockMvc.perform(post("/api/cliente/nuevo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Se ha creado el cliente."))
                .andExpect(jsonPath("$.codigo").value(201));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetClienteById() throws Exception {
        when(clienteService.getById(1)).thenReturn(cliente1);

        mockMvc.perform(get("/api/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gabriel"));
    }
}