package com.clientes_api.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String user;
    private String pass;
}
