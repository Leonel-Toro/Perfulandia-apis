package com.perfulandia.ventas_api.service;

import com.perfulandia.ventas_api.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClienteDTOService {
    @Autowired
    private RestTemplate restTemplate;

    public ClienteDTO findById(Integer idCliente){
        return restTemplate.getForObject("http://localhost:8082/api/cliente/"+idCliente,ClienteDTO.class);
    }
}
