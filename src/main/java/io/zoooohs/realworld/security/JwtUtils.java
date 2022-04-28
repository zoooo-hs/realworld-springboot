package io.zoooohs.realworld.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;

public class JwtUtils {
    private final Long validSeconds;
    private final Key key;

    public JwtUtils(String signKey, Long validSeconds) {
        this.validSeconds = validSeconds;
        key = Keys.hmacShaKeyFor(signKey.getBytes(StandardCharsets.UTF_8));
    }

    public String encode(String sub) {
        if (sub == null || sub.equals("")) {
            return null;
        }
        Instant exp = Instant.now().plusSeconds(validSeconds);
        return Jwts.builder().setSubject(sub).setExpiration(Date.from(exp)).signWith(key).compact();
    }
}
