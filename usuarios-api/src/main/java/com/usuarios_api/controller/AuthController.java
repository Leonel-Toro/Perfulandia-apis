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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
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

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> verUsuario(@PathVariable Integer idUsuario){
        Usuario usuario = authService.findByIdUsuario(idUsuario);
        if(usuario != null){
            return ResponseEntity.status(HttpStatus.FOUND).body(usuario);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
    }

    @GetMapping("/rol/{tipo}")
    public ResponseEntity<?> listaUsuarioByRol(@PathVariable String tipo){
        List<Usuario> usuarios = authService.findUsuariosByRol(tipo);
        if(usuarios != null){
            return ResponseEntity.status(HttpStatus.FOUND).body(usuarios);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay usuarios con este rol.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        System.out.println("➡️ Ejecutando LOGIN");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
            );
            System.out.println("Autenticado correctamente");
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());
            String jwt = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Credenciales inválidas."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout() {
        System.out.println("➡️ Ejecutando LOGOUT");
        return ResponseEntity.ok(new AuthResponse("Sesión cerrada correctamente"));
    }
}
