package com.perfulandia.productos_api.dto;

import com.perfulandia.productos_api.models.Categoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombreProducto;
    private String descripcionProducto;
    private BigDecimal precio;
    private Categoria categoria;
    private String marca;
}
