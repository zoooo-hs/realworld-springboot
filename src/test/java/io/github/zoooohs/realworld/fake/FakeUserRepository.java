package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.application.port.out.persistance.user.UserRepository;
import io.github.zoooohs.realworld.domain.model.User;
import io.github.zoooohs.realworld.domain.model.UserId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {
    private final Map<UserId, User> storage = new HashMap<>();

    public User getByUserName(String username) {
        return storage.values().stream().filter(user -> user.getUsername().equals(username)).findFirst().get();
    }

    @Override
    public void save(User user) {
        storage.put(user.getId(), user);
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
    public User getByUserId(UserId userId) {
        User found = storage.get(userId);
        return returnCopiedUser(found);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage
                .values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .map(this::returnCopiedUser)
                .findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return storage
                .values()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .map(this::returnCopiedUser)
                .findFirst();
    }

    private User returnCopiedUser(User found) {
        if (found == null) return null;
        return User.builder()
                .id(found.getId())
                .username(found.getUsername())
                .bio(found.getBio())
                .email(found.getEmail())
                .image(found.getImage())
                .password(found.getPassword())
                .followings(found.getFollowings() != null
                        ? new ArrayList<>(found.getFollowings())
                        : new ArrayList<>()
                )
                .build();
    }
}
