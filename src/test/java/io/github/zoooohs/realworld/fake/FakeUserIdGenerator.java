package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.application.port.out.persistance.user.UserIdGenerator;
import io.github.zoooohs.realworld.domain.model.user.UserId;

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
