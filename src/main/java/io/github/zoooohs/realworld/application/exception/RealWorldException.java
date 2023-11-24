package io.github.zoooohs.realworld.application.exception;

public abstract class RealWorldException extends RuntimeException {
    public abstract Type type();

    public abstract String getMessage();
    public static enum Type {
        BAD_REQUEST, NOT_FOUND, UNAUTHORIZED, CONFLICT
    }
}
