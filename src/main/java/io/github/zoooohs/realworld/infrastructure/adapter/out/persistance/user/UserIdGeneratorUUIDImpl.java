package io.github.zoooohs.realworld.infrastructure.adapter.out.persistance.user;

import io.github.zoooohs.realworld.domain.user.entity.UserId;
import io.github.zoooohs.realworld.domain.user.service.UserIdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserIdGeneratorUUIDImpl implements UserIdGenerator {
    @Override
    public UserId generate() {
        String uuid = UUID.randomUUID().toString();
        return new UserId(uuid);
    }
}
