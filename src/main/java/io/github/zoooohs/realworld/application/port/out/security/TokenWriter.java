package io.github.zoooohs.realworld.application.port.out.security;

import io.github.zoooohs.realworld.domain.model.User;

public interface TokenWriter {
    String issue(User authenticatedUser);
}
