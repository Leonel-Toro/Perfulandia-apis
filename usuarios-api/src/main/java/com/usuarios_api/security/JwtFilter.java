package com.usuarios_api.security;

import com.usuarios_api.service.UsuarioDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("➡️ Método ejecutado: " + request.getMethod() + " | URI: " + request.getRequestURI());
        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/logout")) {
            filterChain.doFilter(request, response); // omite validación
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String correo = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                jwt = authHeader.substring(7);
                correo = jwtService.extractUsername(jwt);
                UserDetails usuario = usuarioDetailsService.loadUserByUsername(correo);
                if (jwtService.isTokenValid(jwt, usuario)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (Exception error){
                System.out.println("Error al validar JWT: " + error.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
