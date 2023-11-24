package io.github.zoooohs.realworld.application.exception;

public class NoMatchedAuthentication extends RealWorldException {
    @Override
    public String getMessage() {
        return "there is no matched authentication user information. please check again";
    }
    @Override
    public Type type() {
        return Type.NOT_FOUND;
    }
}
