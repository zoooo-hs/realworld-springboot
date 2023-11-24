package io.github.zoooohs.realworld.application.exception;

public class UnauthorizedRequest extends RealWorldException {
    @Override
    public Type type() {
        return Type.UNAUTHORIZED;
    }

    @Override
    public String getMessage() {
        return "unauthorized";
    }
}
