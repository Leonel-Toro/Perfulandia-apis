package com.perfulandia.ventas_api.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CuponResponseDTO {
    private String codigo;
    private boolean valido;
    private BigDecimal descuento;
}
