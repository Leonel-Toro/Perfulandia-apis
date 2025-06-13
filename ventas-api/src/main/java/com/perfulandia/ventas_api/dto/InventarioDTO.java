package com.perfulandia.ventas_api.dto;

public class InventarioDTO {
    private Long idProducto;
    private int cantidad;

    public InventarioDTO() {
    }

    public InventarioDTO(Long idProducto, int cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
