package com.perfulandia.cupones_api.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    private String secretPass = "clave-super-secreta-para-firmar-jwt-123456789-abcdefghijklmnopqrstuvwx";

    public String extractUsername(String token) {
        Key key = Keys.hmacShaKeyFor(secretPass.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public List<String> extractRoles(String token) {
        Key key = Keys.hmacShaKeyFor(secretPass.getBytes(StandardCharsets.UTF_8));
        return (List<String>) Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .get("roles");
    }

    public boolean isTokenValid(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secretPass.getBytes(StandardCharsets.UTF_8));
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Date extractExpiration(String token) {
        Key key = Keys.hmacShaKeyFor(secretPass.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
