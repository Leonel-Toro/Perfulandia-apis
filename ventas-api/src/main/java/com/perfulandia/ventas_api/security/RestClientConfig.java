package com.perfulandia.ventas_api.security;

import com.perfulandia.ventas_api.client.ClienteRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public ClienteRestClient clienteRestClient(RestClient.Builder builder) {
        RestClient restClient = builder.baseUrl("http://localhost:8082/api/cliente").build();

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(ClienteRestClient.class);
    }

}