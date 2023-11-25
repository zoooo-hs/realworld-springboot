package io.github.zoooohs.realworld.application.exception;

public class AlreadyFollowed extends RealWorldException {
    @Override
    public Type type() {
        return Type.CONFLICT;
    }

    @Override
    public String getMessage() {
        return "already followed";
    }
}
