package com.usuarios_api.controller;

import com.usuarios_api.dto.RegistroUsuarioRequest;
import com.usuarios_api.models.Usuario;
import com.usuarios_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class AuthController {
    @Autowired
    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioRequest request) {
        try {
            Usuario nuevoUsuario = authService.registrarUsuario(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
