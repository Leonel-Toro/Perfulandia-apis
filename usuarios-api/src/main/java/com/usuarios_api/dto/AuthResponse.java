package com.usuarios_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String JtwToken;
    private String rol;
    private String correo;
}