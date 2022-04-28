package io.zoooohs.realworld.exception;

import lombok.Getter;

@Getter
public enum Error {
    DUPLICATED_USER("there is duplicated user information");

    private final String message;

    Error(String message) {
        this.message = message;
    }
}
