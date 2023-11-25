package io.github.zoooohs.realworld.application.port.out.persistance.user;

import io.github.zoooohs.realworld.domain.model.UserId;

public interface UserIdGenerator {
    UserId generate();
}
