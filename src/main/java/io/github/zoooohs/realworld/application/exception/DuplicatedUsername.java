package io.github.zoooohs.realworld.application.exception;

public class DuplicatedUsername extends RealWorldException {
    @Override
    public String getMessage() {
        return "username is duplicated, already used";
    }

    @Override
    public Type type() {
        return Type.CONFLICT;
    }
}
