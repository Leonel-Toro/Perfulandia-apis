package com.perfulandia.ventas_api.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.perfulandia.ventas_api.dto.CuponRequestDTO;
import com.perfulandia.ventas_api.dto.CuponResponseDTO;

@HttpExchange(url = "http://localhost:8082/api/cupones")
public interface CuponRestClient {

    @PostExchange("/validar")
    CuponResponseDTO validarCupon(@RequestBody CuponRequestDTO request);
}
