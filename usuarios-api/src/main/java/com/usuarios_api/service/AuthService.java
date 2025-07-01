package com.usuarios_api.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.usuarios_api.dto.RegistroUsuarioRequest;
import com.usuarios_api.models.Rol;
import com.usuarios_api.models.Usuario;
import com.usuarios_api.repository.RolRepository;
import com.usuarios_api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

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

    public Usuario findByIdUsuario(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario.longValue()).orElse(null);
    }

    public List<Usuario> findUsuariosByRol(String tipoRol) {
        return usuarioRepository.findByRolesRol(tipoRol);
    }
}
