package com.clientes_api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthentication implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Personaliza la respuesta HTTP para usuarios no autenticados
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        response.getWriter().write("{\"codigo\":401,\"mensaje\":\"Token inválido o no proporcionado. Acceso denegado.\"}");
    }
}
