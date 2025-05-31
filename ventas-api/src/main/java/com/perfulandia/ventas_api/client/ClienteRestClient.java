package com.perfulandia.ventas_api.client;

import com.perfulandia.ventas_api.dto.ClienteDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url ="http://localhost:8082/api/cliente")
public interface ClienteRestClient {
    @GetExchange("/{id}")
    ClienteDTO findById(@PathVariable Integer id);
}
