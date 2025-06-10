package com.perfulandia.ventas_api.service;

import com.perfulandia.ventas_api.models.DetalleVenta;
import com.perfulandia.ventas_api.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetalleVentaService {
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        validarDetalle(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    // Validación básica
    private void validarDetalle(DetalleVenta detalle) {
        if (detalle.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor que cero.");
        }
        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio unitario debe ser mayor que cero.");
        }
        if (detalle.getIdProducto() == null) {
            throw new RuntimeException("El producto no puede ser nulo.");
        }
        if (detalle.getVenta() == null) {
            throw new RuntimeException("La venta asociada no puede ser nula.");
        }
    }

}
