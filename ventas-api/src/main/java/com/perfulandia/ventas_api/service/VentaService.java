package com.perfulandia.ventas_api.service;

import com.perfulandia.ventas_api.client.ClienteRestClient;
import com.perfulandia.ventas_api.dto.ClienteDTO;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    private final ClienteRestClient clienteRest;

    public VentaService(ClienteRestClient clienteRest){
        this.clienteRest = clienteRest;
    }

    public Venta procesarVenta(Venta nuevaVenta){
        ClienteDTO cliente = clienteRest.findById(nuevaVenta.getIdCliente());
        if(cliente == null){
            return null;
        }

        return ventaRepository.save(nuevaVenta);
    }

    public Venta save(Venta venta){
        return procesarVenta(venta);
    }

    public List<Venta> getVentas(){
        return ventaRepository.findAll();
    }

    public Venta findById(Long id){
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.orElse(null);
    }

    public List<Venta> getVentasByIdCliente(Long idCliente) {
        return ventaRepository.findByIdCliente(idCliente);
    }

}
