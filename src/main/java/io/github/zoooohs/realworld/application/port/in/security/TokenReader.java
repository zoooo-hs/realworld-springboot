package io.github.zoooohs.realworld.application.port.in.security;

import io.github.zoooohs.realworld.domain.model.UserId;

public interface TokenReader {
    UserId getUserId(String token);
}
