package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.domain.user.entity.UserId;
import io.github.zoooohs.realworld.domain.user.service.UserIdGenerator;

public class FakeUserIdGenerator implements UserIdGenerator {
    private Long currentId;

    public FakeUserIdGenerator(Long currentId) {
        this.currentId = currentId;
    }

    public Long getCurrentId() {
        return currentId;
    }

    @Override
    public UserId generate() {
        currentId++;
        return new UserId(currentId.toString());
    }
}
