package io.github.zoooohs.realworld.application.model.user;

import io.github.zoooohs.realworld.domain.user.entity.UserId;

public record TokenPayload(UserId userId, String username, String email) {
}
