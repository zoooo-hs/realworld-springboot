package io.github.zoooohs.realworld.infrastructure.adapter.out.security;

import io.github.zoooohs.realworld.application.port.out.security.PasswordManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordManagerImpl implements PasswordManager {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encrypt(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean match(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
