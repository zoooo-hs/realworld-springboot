package io.github.zoooohs.realworld.application.port.out.security;

import io.github.zoooohs.realworld.domain.model.user.User;

public interface TokenWriter {
    String issue(User authenticatedUser);
}
