package io.github.zoooohs.realworld.application.port.in.security;

import io.github.zoooohs.realworld.domain.UserId;

public interface TokenReader {
    UserId getUserId(String token);
}
