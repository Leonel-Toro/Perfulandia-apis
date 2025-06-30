package com.perfulandia.ventas_api.dto;

import com.perfulandia.ventas_api.models.DetalleVenta;
import com.perfulandia.ventas_api.models.Venta;

public class VentaRequestDTO {

    private Venta venta;
    private DetalleVenta detalleVenta;

    public VentaRequestDTO() {}

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public DetalleVenta getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(DetalleVenta detalleVenta) {
        this.detalleVenta = detalleVenta;
    }
}
