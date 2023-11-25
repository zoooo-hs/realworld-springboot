package io.github.zoooohs.realworld.application.exception;

public class NotFollowing extends RealWorldException {
    @Override
    public Type type() {
        return Type.CONFLICT;
    }

    @Override
    public String getMessage() {
        return "you don't follow the user";
    }
}
