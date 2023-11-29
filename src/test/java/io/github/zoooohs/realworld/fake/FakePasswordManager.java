package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.domain.user.service.PasswordManager;

public class FakePasswordManager implements PasswordManager {
    private final String prefix;

    public FakePasswordManager(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String encrypt(String rawPassword) {
        return prefix+rawPassword;
    }

    @Override
    public boolean match(String rawPassword, String encryptedPassword) {
        return encryptedPassword.substring(prefix.length()).equals(rawPassword);
    }
}
