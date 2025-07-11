package com.usuarios_api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuarios_api.dto.AuthResponse;
import com.usuarios_api.dto.LoginRequest;
import com.usuarios_api.dto.RegistroUsuarioRequest;
import com.usuarios_api.models.Usuario;
import com.usuarios_api.security.JwtService;
import com.usuarios_api.service.AuthService;
import com.usuarios_api.service.UsuarioDetailsServiceImpl;

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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ✅ Normal: registrar usuario
    @PostMapping("/")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioRequest request) {
        try {
            Usuario nuevoUsuario = authService.registrarUsuario(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(201, "Se ha creado nuevo usuario.", "Correo: " + nuevoUsuario.getCorreo()));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // ✅ Normal: ver usuario por ID
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> verUsuario(@PathVariable Integer idUsuario) {
        Usuario usuario = authService.findByIdUsuario(idUsuario);
        if (usuario != null) {
            return ResponseEntity.status(HttpStatus.FOUND).body(usuario);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
    }

    // ✅ Normal: lista usuarios por rol
    @GetMapping("/rol/{tipo}")
    public ResponseEntity<?> listaUsuarioByRol(@PathVariable String tipo) {
        List<Usuario> usuarios = authService.findUsuariosByRol(tipo);
        if (usuarios != null) {
            return ResponseEntity.status(HttpStatus.FOUND).body(usuarios);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay usuarios con este rol.");
    }

    // ✅ Normal: login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());
            String jwt = jwtService.generateToken(userDetails);

            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(200, "Se ha iniciado sesión.", jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(401, "Credenciales inválidas."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(200, "Sesión cerrada correctamente"));
    }

    // ✅ HATEOAS: lista todos
    @GetMapping("/hateoas/")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> getAllHateoas() {
        List<Usuario> usuarios = authService.findUsuariosByRol("ADMIN"); // Opcional: obtén todos
        List<EntityModel<Usuario>> usuariosModel = usuarios.stream()
                .map(u -> EntityModel.of(
                        u,
                        linkTo(methodOn(AuthController.class).verUsuarioHateoas(u.getId().intValue())).withRel("usuario-por-id")
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        usuariosModel,
                        linkTo(methodOn(AuthController.class).getAllHateoas()).withRel("lista-usuarios")
                )
        );
    }

    // ✅ HATEOAS: ver uno por ID
    @GetMapping("/hateoas/usuario/{idUsuario}")
    public ResponseEntity<?> verUsuarioHateoas(@PathVariable Integer idUsuario) {
        Usuario usuario = authService.findByIdUsuario(idUsuario);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        EntityModel<Usuario> usuarioModel = EntityModel.of(
                usuario,
                linkTo(methodOn(AuthController.class).verUsuarioHateoas(idUsuario)).withRel("usuario-por-id"),
                linkTo(methodOn(AuthController.class).getAllHateoas()).withRel("lista-usuarios")
        );

        return ResponseEntity.ok(usuarioModel);
    }

    // ✅ HATEOAS: lista por rol
    @GetMapping("/hateoas/rol/{tipo}")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listaUsuarioByRolHateoas(@PathVariable String tipo) {
        List<Usuario> usuarios = authService.findUsuariosByRol(tipo);
        if (usuarios == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<EntityModel<Usuario>> usuariosModel = usuarios.stream()
                .map(u -> EntityModel.of(
                        u,
                        linkTo(methodOn(AuthController.class).verUsuarioHateoas(u.getId().intValue())).withRel("usuario-por-id")
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(
                        usuariosModel,
                        linkTo(methodOn(AuthController.class).listaUsuarioByRolHateoas(tipo)).withRel("usuarios-por-rol")
                )
        );
    }

    // ✅ HATEOAS: crear
    @PostMapping("/hateoas")
    public ResponseEntity<?> registrarUsuarioHateoas(@RequestBody RegistroUsuarioRequest request) {
        try {
            Usuario nuevoUsuario = authService.registrarUsuario(request);

            EntityModel<Usuario> usuarioModel = EntityModel.of(
                    nuevoUsuario,
                    linkTo(methodOn(AuthController.class).verUsuarioHateoas(nuevoUsuario.getId().intValue())).withRel("usuario-por-id"),
                    linkTo(methodOn(AuthController.class).getAllHateoas()).withRel("lista-usuarios")
            );

            return ResponseEntity
                    .created(linkTo(methodOn(AuthController.class).verUsuarioHateoas(nuevoUsuario.getId().intValue())).toUri())
                    .body(usuarioModel);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
