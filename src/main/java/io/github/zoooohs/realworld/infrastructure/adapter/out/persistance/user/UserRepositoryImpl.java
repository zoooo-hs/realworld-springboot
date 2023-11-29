package io.github.zoooohs.realworld.infrastructure.adapter.out.persistance.user;

import io.github.zoooohs.realworld.domain.user.entity.User;
import io.github.zoooohs.realworld.domain.user.entity.UserId;
import io.github.zoooohs.realworld.domain.user.service.UserRepository;
import io.github.zoooohs.realworld.infrastructure.adapter.out.persistance.user.jpa.UserJpaRepository;
import io.github.zoooohs.realworld.infrastructure.model.persistance.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUserName(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public void save(User user) {
        UserEntity userEntity = UserEntity.fromDomainEntity(user);
        userJpaRepository.save(userEntity);
    }

    @Override
    public User getByUserId(UserId currentUserId) {
        return userJpaRepository.getReferenceById(currentUserId.id()).toDomainEntity();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toDomainEntity);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(UserEntity::toDomainEntity);
    }

    @Override
    public Optional<User> findByUserId(UserId userId) {
        return userJpaRepository.findById(userId.id()).map(UserEntity::toDomainEntity);
    }
}
