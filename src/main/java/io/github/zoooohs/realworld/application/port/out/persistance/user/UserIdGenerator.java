package io.github.zoooohs.realworld.application.port.out.persistance.user;

import io.github.zoooohs.realworld.domain.model.user.UserId;

public interface UserIdGenerator {
    UserId generate();
}
