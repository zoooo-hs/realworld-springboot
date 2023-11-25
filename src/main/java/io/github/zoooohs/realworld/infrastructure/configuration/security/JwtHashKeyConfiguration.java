package io.github.zoooohs.realworld.infrastructure.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtHashKeyConfiguration {
    @Bean
    public JwtProperties jwtProperties(
            @Value("${security.jwt.key}") String key,
            @Value("${security.jwt.expiration}") Long expiration
    ) {
        return new JwtProperties(key, expiration);
    }
}
