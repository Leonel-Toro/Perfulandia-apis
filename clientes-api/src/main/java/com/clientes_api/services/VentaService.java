package com.clientes_api.services;

import com.clientes_api.repository.ClienteRepository;
import com.clientes_api.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;



}
