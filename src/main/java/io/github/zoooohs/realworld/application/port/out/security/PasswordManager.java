package io.github.zoooohs.realworld.application.port.out.security;

public interface PasswordManager {
    String encrypt(String rawPassword);

    boolean match(String rawPassword, String encryptedPassword);
}
