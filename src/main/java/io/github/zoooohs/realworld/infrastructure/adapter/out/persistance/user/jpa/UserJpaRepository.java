package io.github.zoooohs.realworld.infrastructure.adapter.out.persistance.user.jpa;

import io.github.zoooohs.realworld.infrastructure.model.persistance.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);
}
