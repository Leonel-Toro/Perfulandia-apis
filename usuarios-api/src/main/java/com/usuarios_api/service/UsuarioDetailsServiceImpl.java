package com.usuarios_api.service;

import com.usuarios_api.models.Usuario;
import com.usuarios_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        //Se busca usuario con el mismo mail en la BBDD, en caso de no encontrar lanza una excepcion
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        //Se crea una lista de roles que el usuario tiene asignado
        List<GrantedAuthority> roles = usuario.getRoles().stream().map(rol -> new SimpleGrantedAuthority("ROLE_"+rol.getRol()))
                .collect(Collectors.toList());
        //retorna los detalles del usuario
        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreo(),
                usuario.getPassword(),
                roles);
    }

}
