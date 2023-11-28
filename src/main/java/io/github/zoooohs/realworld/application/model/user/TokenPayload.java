package io.github.zoooohs.realworld.application.model.user;

import io.github.zoooohs.realworld.domain.model.user.UserId;

public record TokenPayload(UserId userId, String username, String email) {
}
