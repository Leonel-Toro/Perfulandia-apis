package com.clientes_api.clients;

import com.clientes_api.dto.RegistroClienteDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "http://localhost:8081/")
public interface UsuarioRestClient {
    @PostExchange
    void registrarUsuario(@RequestBody RegistroClienteDTO request);
}
