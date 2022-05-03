package io.zoooohs.realworld.exception;

import lombok.Getter;

@Getter
public enum Error {
    DUPLICATED_USER("there is duplicated user information"),
    LOGIN_INFO_INVALID("login information is invalid"),
    ;

    private final String message;

    Error(String message) {
        this.message = message;
    }
}
