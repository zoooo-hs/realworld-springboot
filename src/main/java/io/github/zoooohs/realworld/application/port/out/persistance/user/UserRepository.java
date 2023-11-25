package io.github.zoooohs.realworld.application.port.out.persistance.user;

import io.github.zoooohs.realworld.domain.model.User;
import io.github.zoooohs.realworld.domain.model.UserId;

import java.util.Optional;

public interface UserRepository {
    boolean existsByEmail(String email);
    boolean existsByUserName(String username);

    void save(User user);

    User getByUserId(UserId currentUserId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
