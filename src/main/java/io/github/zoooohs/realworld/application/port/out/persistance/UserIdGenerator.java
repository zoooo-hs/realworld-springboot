package io.github.zoooohs.realworld.application.port.out.persistance;

import io.github.zoooohs.realworld.domain.UserId;

public interface UserIdGenerator {
    UserId generate();
}
