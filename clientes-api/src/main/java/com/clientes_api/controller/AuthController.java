package com.clientes_api.controller;

import com.clientes_api.services.AuthService;
import com.clientes_api.dto.AuthResponse;
import com.clientes_api.dto.LoginRequest;
import com.clientes_api.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/logout")
    public String logout() {
        // El cliente debe borrar el token (no hay estado del lado del servidor)
        return "Logout exitoso";
    }
}
