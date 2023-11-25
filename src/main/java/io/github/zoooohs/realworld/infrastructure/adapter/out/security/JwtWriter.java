package io.github.zoooohs.realworld.infrastructure.adapter.out.security;

import io.github.zoooohs.realworld.application.port.out.security.TokenWriter;
import io.github.zoooohs.realworld.domain.model.User;
import io.github.zoooohs.realworld.infrastructure.configuration.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtWriter implements TokenWriter {
    private final SecretKey secretKey;
    private final Long expiration;

    public JwtWriter(JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.originalSecretKey().getBytes());
        this.expiration = jwtProperties.expiration();
    }
    @Override
    public String issue(User authenticatedUser) {
        Claims claims = createClaims(authenticatedUser);
        return createToken(claims);
    }

    private String createToken(Claims claims) {
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    private Claims createClaims(User authenticatedUser) {
        Date iat = new Date();
        return Jwts.claims()
                .subject(authenticatedUser.getId().id().toString())
                .issuedAt(iat)
                .expiration(getExp(iat))
                .add("username", authenticatedUser.getUsername())
                .add("email", authenticatedUser.getEmail())
                .build();
    }

    private Date getExp(Date iat) {
        return new Date(iat.getTime() + expiration);
    }
}
