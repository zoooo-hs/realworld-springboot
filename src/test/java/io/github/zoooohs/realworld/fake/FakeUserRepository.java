package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.application.port.out.persistance.UserRepository;
import io.github.zoooohs.realworld.domain.model.User;
import io.github.zoooohs.realworld.domain.model.UserId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {
    private final Map<UserId, User> storage = new HashMap<>();

    public User getByUserName(String username) {
        return storage.values().stream().filter(user -> user.getUsername().equals(username)).findFirst().get();
    }
    @Override
    public boolean existsByEmail(String email) {
        return storage.values().stream().map(User::getEmail).anyMatch(email::equals);
    }

    @Override
    public boolean existsByUserName(String username) {
        return storage.values().stream().map(User::getUsername).anyMatch(username::equals);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public User getByUserId(UserId userId) {
        return storage.get(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return storage.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    @Override
    public void save(User user) {
        storage.put(user.getId(), user);
    }
}
