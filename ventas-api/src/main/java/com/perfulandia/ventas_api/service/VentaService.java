package com.perfulandia.ventas_api.service;

import com.clientes_api.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;



}
