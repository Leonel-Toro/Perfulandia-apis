package com.perfulandia.productos_api.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String jwt = httpServletRequest.getHeader("Authorization");
        if (jwt != null) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, jwt);
        }
        return execution.execute(request, body);
    }
}
