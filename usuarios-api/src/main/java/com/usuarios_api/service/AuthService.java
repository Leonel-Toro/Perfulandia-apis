package com.usuarios_api.service;

import com.usuarios_api.dto.RegistroUsuarioRequest;
import com.usuarios_api.models.Rol;
import com.usuarios_api.models.Usuario;
import com.usuarios_api.repository.RolRepository;
import com.usuarios_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public Usuario registrarUsuario(RegistroUsuarioRequest request) {
        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("Correo ya registrado");
        }

        Rol rol = rolRepository.findByRol(request.getRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        String passCifrada = passwordEncoder.encode(request.getPassword());

        Usuario usuario = new Usuario();
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passCifrada);
        usuario.getRoles().add(rol);
        usuarioRepository.save(usuario);
        return usuario;
    }
}
