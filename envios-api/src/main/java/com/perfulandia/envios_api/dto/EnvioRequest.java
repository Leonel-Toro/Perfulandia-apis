package com.perfulandia.envios_api.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class EnvioRequest {
    private String direccion;
    private String estado;
    private Date fecha;
    private String transportista;
    private String tracking;
}
