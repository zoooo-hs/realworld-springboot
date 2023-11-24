package io.github.zoooohs.realworld.application.exception;

public class DuplicatedEmail extends RealWorldException {
    @Override
    public String getMessage() {
        return "email is duplicated, already used";
    }

    @Override
    public Type type() {
        return Type.CONFLICT;
    }
}
