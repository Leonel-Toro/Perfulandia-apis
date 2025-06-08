package com.usuarios_api.controller;

import com.usuarios_api.dto.AuthResponse;
import com.usuarios_api.dto.LoginRequest;
import com.usuarios_api.dto.RegistroUsuarioRequest;
import com.usuarios_api.models.Usuario;
import com.usuarios_api.service.AuthService;
import com.usuarios_api.security.JwtService;
import com.usuarios_api.service.UsuarioDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtService jwtService;

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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());
            String jwt = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Credenciales inválidas."));
        }
    }
}
