package io.zoooohs.realworld.domain.user.repository;

import io.zoooohs.realworld.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username OR u.email = :email")
    List<UserEntity> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
}
