package com.clientes_api.services;

import com.clientes_api.models.Cliente;
import com.clientes_api.models.Usuario;
import com.clientes_api.repository.UsuarioRepository;
import com.clientes_api.security.JwtUtil;
import com.clientes_api.dto.AuthResponse;
import com.clientes_api.dto.LoginRequest;
import com.clientes_api.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUser(), request.getPass()));

        Usuario usuario = usuarioRepository.findByUser(request.getUser())
                .orElseThrow();

        String token = jwtUtil.generateToken(usuario.getUser());
        return new AuthResponse(token, usuario.getUser());
    }

    public String register(RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setUser(request.getUser());
        usuario.setPassword(passwordEncoder.encode(request.getPass()));
        usuario.setEstado(true);

        // Persona solo se referencia por id, para simplificar
        Cliente cliente = new Cliente();
        cliente.setId(request.getIdPersona());
        usuario.setCliente(cliente);

        usuarioRepository.save(usuario);
        return "Usuario registrado con éxito";
    }
}
