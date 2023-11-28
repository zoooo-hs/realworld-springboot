package io.github.zoooohs.realworld.infrastructure.adapter.in.security;

import io.github.zoooohs.realworld.application.model.user.TokenPayload;
import io.github.zoooohs.realworld.application.port.in.security.TokenReader;
import io.github.zoooohs.realworld.domain.model.user.UserId;
import io.github.zoooohs.realworld.infrastructure.configuration.security.JwtProperties;
import io.github.zoooohs.realworld.infrastructure.exception.TokenExpired;
import io.github.zoooohs.realworld.infrastructure.exception.TokenParseError;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtReader implements TokenReader {
    private final JwtParser parser;

    public JwtReader(JwtProperties jwtProperties) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.originalSecretKey().getBytes());
        this.parser = Jwts.parser().verifyWith(secretKey).build();
    }

    @Override
    public TokenPayload read(String token) {
        try {
            Claims payload = parseToken(token);
            return payloadToTokenPayload(payload);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private TokenPayload payloadToTokenPayload(Claims payload) {
        String username = payload.get("username", String.class);
        String email = payload.get("email", String.class);
        UserId userId = getUserId(payload);
        return new TokenPayload(userId, username, email);
    }

    private Claims parseToken(String token) {
        try {
            Jws<Claims> claimsJws = parser.parseSignedClaims(token);
            return claimsJws.getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenExpired();
        } catch (Exception e) {
            throw new TokenParseError();
        }
    }

    private UserId getUserId(Claims payload) {
        String subject = payload.getSubject();
        return new UserId(subject);
    }
}
