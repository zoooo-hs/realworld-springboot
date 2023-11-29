package io.github.zoooohs.realworld.domain.user.service;

import io.github.zoooohs.realworld.domain.user.entity.User;
import io.github.zoooohs.realworld.domain.user.entity.UserId;

import java.util.Optional;

public interface UserRepository {
    boolean existsByEmail(String email);
    boolean existsByUserName(String username);

    void save(User user);

    User getByUserId(UserId currentUserId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(UserId userId);
}
