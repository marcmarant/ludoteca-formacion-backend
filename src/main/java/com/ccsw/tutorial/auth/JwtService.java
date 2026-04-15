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

    public String createToken(User user) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("username", user.getUsername())
                .issuedAt(new Date(now))
                .expiration(new Date(now + this.expiration))
                .signWith(this.key)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUserId(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isValid(String token) {
        parseToken(token);
        return true;
    }
}
