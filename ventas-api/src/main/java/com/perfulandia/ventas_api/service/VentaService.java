package com.perfulandia.ventas_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.perfulandia.ventas_api.dto.InventarioDTO;
import com.perfulandia.ventas_api.models.DetalleVenta;
import com.perfulandia.ventas_api.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.ventas_api.dto.ClienteDTO;
import com.perfulandia.ventas_api.models.Vendedor;
import com.perfulandia.ventas_api.models.Venta;
import com.perfulandia.ventas_api.repository.VentaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteDTOService clienteDTOService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Venta procesarVenta(Venta nuevaVenta, DetalleVenta detalleVenta){
        try {
            ClienteDTO cliente = clienteDTOService.findById(nuevaVenta.getIdCliente());
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente no asignado.");
            }

            if (nuevaVenta.getVendedor() == null) {
                throw new IllegalArgumentException("Vendedor no asignado.");
            }

            if (nuevaVenta.getFecha() == null) {
                throw new IllegalArgumentException("La fecha no puede estar vacía.");
            }

            if(detalleVenta.getIdProducto() == null || detalleVenta.getIdProducto().equals("")){
                throw new IllegalArgumentException("El producto no puede ser vacio.");
            }

            if(detalleVenta.getCantidad() > 0){
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }

            if(detalleVenta.getPrecioUnitario() == null || detalleVenta.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0){
                throw new IllegalArgumentException("El precio unitario debe ser mayor a 0.");
            }
            BigDecimal total = detalleVenta.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleVenta.getCantidad()));
            nuevaVenta.setTotal(total);

            try {
                restTemplate.put(
                        "http://localhost:8084/api/inventario/ajustar",
                        new InventarioDTO(detalleVenta.getIdProducto(), detalleVenta.getCantidad())
                );
            }catch (HttpStatusCodeException e) {
                System.err.println("Error HTTP al ajustar inventario: " + e.getStatusCode());
                System.err.println("Respuesta del servidor: " + e.getResponseBodyAsString());
                throw new RuntimeException("No se pudo ajustar el inventario: " + e.getMessage(), e);
            }
            ventaRepository.save(nuevaVenta);
            detalleVenta.setVenta(nuevaVenta);
            detalleVentaRepository.save(detalleVenta);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la venta: " + e.getMessage(), e);
        }
        return nuevaVenta;
    }

    public List<Venta> getVentas() {
        return ventaRepository.findAll();
    }

    public Venta findById(Long id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.orElse(null);
    }

    public List<Venta> getVentasByIdCliente(Long idCliente) {
        return ventaRepository.findByIdCliente(idCliente);
    }

    public List<Venta> getVentasByIdVendedor(Vendedor vendedor){
        List<Venta> ventasVendedor = ventaRepository.findByVendedor(vendedor);
        return ventasVendedor;
    }
}
