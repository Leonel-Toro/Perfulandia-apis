package com.usuarios_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private int codigo;
    private String mensaje;
    private String credenciales;

    public AuthResponse(int codigo,String mensaje){
        this.codigo = codigo;
        this.mensaje = mensaje;
    }
}