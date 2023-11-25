package io.github.zoooohs.realworld.infrastructure.configuration.security;

public record JwtProperties(String originalSecretKey, Long expiration) {
}
