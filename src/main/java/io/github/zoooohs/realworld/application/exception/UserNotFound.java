package io.github.zoooohs.realworld.application.exception;

public class UserNotFound extends RealWorldException {
    @Override
    public Type type() {
        return Type.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "user not found";
    }
}
