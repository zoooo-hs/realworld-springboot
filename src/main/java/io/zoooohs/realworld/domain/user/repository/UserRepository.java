package io.zoooohs.realworld.domain.user.repository;

import io.zoooohs.realworld.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.name = :name OR u.email = :email")
    List<UserEntity> findByNameOrEmail(@Param("name") String name, @Param("email") String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByName(String name);
}
