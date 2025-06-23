package com.perfulandia.envios_api.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class EnvioResponse {
    private int id;
    private String estado;
    private Date fecha;
    private String transportista;
    private String tracking;
    private String direccion;
}
