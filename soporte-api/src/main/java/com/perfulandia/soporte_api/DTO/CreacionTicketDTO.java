package com.perfulandia.soporte_api.DTO;

import java.sql.Date;

import lombok.Data;

@Data
public class CreacionTicketDTO {

    private String asunto;
    private String mensaje;
    private String estado;
    private Date fechaCreacion;

    public CreacionTicketDTO() {
    }
    
}
