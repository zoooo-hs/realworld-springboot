package io.github.zoooohs.realworld.infrastructure.adapter.out.persistance.user;

import io.github.zoooohs.realworld.application.port.out.persistance.user.UserIdGenerator;
import io.github.zoooohs.realworld.domain.model.UserId;
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
