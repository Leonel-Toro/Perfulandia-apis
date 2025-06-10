package com.perfulandia.ventas_api.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.perfulandia.ventas_api.dto.EnvioRequest;
import com.perfulandia.ventas_api.dto.EnvioResponse;

@HttpExchange("/envios")
public interface EnvioClient {

    @PostExchange
    EnvioResponse crearEnvio(@RequestBody EnvioRequest envioRequest);
}
