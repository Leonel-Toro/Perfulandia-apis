package com.perfulandia.ventas_api.client;

import com.perfulandia.ventas_api.dto.RegistroUsuarioRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "http://localhost:8081/")
public interface UsuarioRestClient {
    @PostExchange
    void registrarUsuario(@RequestBody RegistroUsuarioRequest request);
}
