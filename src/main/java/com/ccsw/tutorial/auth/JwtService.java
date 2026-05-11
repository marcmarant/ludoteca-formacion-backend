package com.ccsw.tutorial.auth;

import com.ccsw.tutorial.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Clase que maneja los tokens JWT
 *
 * @author Marcos Martínez Antón
 */
@Service
public class JwtService {

    private final Key key;
    private final long expiration;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-ms}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    /**
     * Crea un token JWT con la información del usuario.
     *
     * @param user usuario a autenticar
     * @return token JWT
     */
    public String createToken(User user) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                .issuedAt(new Date(now))
                .expiration(new Date(now + this.expiration))
                .signWith(this.key)
                .compact();
    }

    /**
     * Parsea un token JWT y devuelve sus claims. Si el token no es válido, lanzará una excepción.
     *
     * @param token a validar
     * @return claims del token
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtiene el id de usuario del token JWT. Si el token no es válido, lanzará una excepción.
     *
     * @param token
     * @return id de usuario
     */
    public String getUserId(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * Valida un token JWT. Si el token no es válido, lanzará una excepción.
     *
     * @param token
     * @return true si es valido
     */
    public boolean isValid(String token) {
        parseToken(token);
        return true;
    }
}
