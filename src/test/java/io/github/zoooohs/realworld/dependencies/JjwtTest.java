package io.github.zoooohs.realworld.dependencies;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JjwtTest {
    @DisplayName("jwt 구성에 쓰이는 key string 만 있으면 매번 새로 key 를 만들어내서 파싱 가능하다.")
    @Test
    void jwt_is_able_to_parse_with_regenerated_key() {
        Date iat = new Date();
        Date exp = new Date(iat.getTime()+1000*60*60*24);
        Claims claims = Jwts.claims()
                .subject("1")
                .issuedAt(iat)
                .expiration(exp)
                .build();

        String key = "k4C*D10T2VCme3b^XH0AzipmvvK2K9eG";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();

        SecretKey regenerated = Keys.hmacShaKeyFor(key.getBytes());
        String jws = Jwts.builder()
                .claims(claims)
                .signWith(regenerated, Jwts.SIG.HS256)
                .compact();
        Jws<Claims> claimsJws = parser.parseSignedClaims(jws);

        assertAll(
                () -> assertThat(claimsJws.getPayload().getSubject()).isEqualTo("1"),
                () -> assertThat(claimsJws.getPayload().getExpiration().before(iat)).isFalse()
        );
    }

    @Test
    void parse_expired_token_throws_validation_error() {
        Date iat = new Date();
        Date exp = new Date(iat.getTime()-10000);
        Claims claims = Jwts.claims()
                .subject("1")
                .issuedAt(iat)
                .expiration(exp)
                .build();

        String key = "k4C*D10T2VCme3b^XH0AzipmvvK2K9eG";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();

        SecretKey regenerated = Keys.hmacShaKeyFor(key.getBytes());
        String jws = Jwts.builder()
                .claims(claims)
                .signWith(regenerated, Jwts.SIG.HS256)
                .compact();

        assertThrows(ExpiredJwtException.class, () -> parser.parseSignedClaims(jws));
    }

    @Test
    void parse_with_invalid_key_throws_validation_error() {
        Date iat = new Date();
        Date exp = new Date(iat.getTime()-10000);
        Claims claims = Jwts.claims()
                .subject("1")
                .issuedAt(iat)
                .expiration(exp)
                .build();

        String key = "k4C*D10T2VCme3b^XH0AzipmvvK2K9eG";
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        SecretKey wrongKey = Keys.hmacShaKeyFor("ssssssssssssssssssssssssssssssss".getBytes());
        JwtParser parser = Jwts.parser().verifyWith(wrongKey).build();

        String jws = Jwts.builder()
                .claims(claims)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

        assertThrows(SignatureException.class, () -> parser.parseSignedClaims(jws));
    }
}
