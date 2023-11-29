package io.github.zoooohs.realworld.domain.user.service;

import io.github.zoooohs.realworld.domain.user.entity.UserId;

public interface UserIdGenerator {
    UserId generate();
}
