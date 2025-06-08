package com.usuarios_api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    /*
    @Value("${security.jwt.contraseniaSecreta}")
    private String secretPass;

    @Value("${security.jwt.expiration}")
    private Long expiracionToken;
    */
    private String secretPass = "clave-super-secreta-para-firmar-jwt-123456789-abcdefghijklmnopqrstuvwx";

    public String generateToken(UserDetails userDetails) {
        Key key = Keys.hmacShaKeyFor(secretPass.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // en nuestro caso: correo
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public String extractUsername(String token) {
        Key key = Keys.hmacShaKeyFor(secretPass.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Key key = Keys.hmacShaKeyFor(secretPass.getBytes(StandardCharsets.UTF_8));

        Date expiration = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
