package com.perfulandia.ventas_api.service;

import com.perfulandia.ventas_api.client.ClienteRestClient;
import com.perfulandia.ventas_api.dto.ClienteDTO;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ClienteRestClient clienteRest;

    public VentaService(ClienteRestClient clienteRest){
        this.clienteRest = clienteRest;
    }

    public Venta procesarVenta(Venta nuevaVenta){
        try {
            ClienteDTO cliente = clienteRest.findById(nuevaVenta.getIdCliente());
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente no asignado.");
            }

            if (nuevaVenta.getVendedor() == null) {
                throw new IllegalArgumentException("Vendedor no asignado.");
            }

            if (nuevaVenta.getFecha() == null) {
                throw new IllegalArgumentException("La fecha no puede estar vacía.");
            }

            if (nuevaVenta.getTotal() == null || nuevaVenta.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El total no puede ser nulo, negativo o cero.");
            }

            return ventaRepository.save(nuevaVenta);

        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la venta: " + e.getMessage(), e);
        }
    }

    public List<Venta> getVentas(){
        return ventaRepository.findAll();
    }

    public Venta findById(Long id){
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.orElse(null);
    }

    public List<Venta> getVentasByIdCliente(Long idCliente) {
        List<Venta> ventasCliente = ventaRepository.findByIdCliente(idCliente);
        return ventasCliente;
    }

    public List<Venta> getVentasByIdVendedor(Vendedor vendedor){
        List<Venta> ventasVendedor = ventaRepository.findByVendedor(vendedor);
        return ventasVendedor;
    }



}
