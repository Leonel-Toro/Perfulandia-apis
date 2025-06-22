package com.perfulandia.ventas_api.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioResponse {
    private Long id;
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private String estadoEnvio;
    private LocalDate fechaEnvio;
    private Long ventaId;
}
