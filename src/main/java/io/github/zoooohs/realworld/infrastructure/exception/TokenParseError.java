package io.github.zoooohs.realworld.infrastructure.exception;

import io.github.zoooohs.realworld.application.exception.RealWorldException;

public class TokenParseError extends RealWorldException {
    @Override
    public Type type() {
        return Type.UNAUTHORIZED;
    }

    @Override
    public String getMessage() {
        return "invalidate token";
    }
}
