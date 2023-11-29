package io.github.zoooohs.realworld.domain.user.service;

public interface PasswordManager {
    String encrypt(String rawPassword);

    boolean match(String rawPassword, String encryptedPassword);
}
