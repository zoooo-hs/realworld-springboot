package io.github.zoooohs.realworld.application.port.in.security;

import io.github.zoooohs.realworld.application.model.user.TokenPayload;

public interface TokenReader {
    TokenPayload read(String token);
}
