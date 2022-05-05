package io.zoooohs.realworld.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonTypeName("errors")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class ErrorMessages {
    private final List<String> body;

    public ErrorMessages() {
        body = new ArrayList<>();
    }

    public void append(String message) {
        body.add(message);
    }
}
