package com.perfulandia.ventas_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.client.RestTemplate;



@Configuration
public class RestClientConfig {
    @Autowired
    private JwtRestTemplateInterceptor interceptor;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.additionalInterceptors(interceptor).build();
    }
}