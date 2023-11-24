package io.github.zoooohs.realworld.application.port.out.persistance;

import io.github.zoooohs.realworld.domain.User;
import io.github.zoooohs.realworld.domain.UserId;

import java.util.Optional;

public interface UserRepository {
    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

    void save(User user);

    Optional<User> findByEmail(String email);

    User getByUserId(UserId currentUserId);
}
