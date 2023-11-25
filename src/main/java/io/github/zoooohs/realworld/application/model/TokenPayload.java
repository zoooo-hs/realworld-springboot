package io.github.zoooohs.realworld.application.model;

import io.github.zoooohs.realworld.domain.model.UserId;

public record TokenPayload(UserId userId, String username, String email) {
}
