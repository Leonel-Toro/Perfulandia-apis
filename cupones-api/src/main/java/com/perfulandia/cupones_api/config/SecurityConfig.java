package com.perfulandia.cupones_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()  // todas las peticiones requieren autenticación
            )
            .httpBasic(withDefaults())  // habilita autenticación básica HTTP
            .csrf(csrf -> csrf.disable()); // deshabilita CSRF para facilitar pruebas con Postman (opcional)

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
            .username("user")
            .password("{noop}password")  // contraseña en texto plano para pruebas
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}
