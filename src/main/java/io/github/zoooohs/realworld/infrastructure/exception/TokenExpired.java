package io.github.zoooohs.realworld.infrastructure.exception;

import io.github.zoooohs.realworld.application.exception.RealWorldException;

public class TokenExpired extends RealWorldException {
    @Override
    public Type type() {
        return Type.UNAUTHORIZED;
    }

    @Override
    public String getMessage() {
        return "token has been expried";
    }
}
