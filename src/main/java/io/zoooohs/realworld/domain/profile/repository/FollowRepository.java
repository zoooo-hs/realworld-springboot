package io.zoooohs.realworld.domain.profile.repository;

import io.zoooohs.realworld.domain.profile.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    Optional<FollowEntity> findByFolloweeIdAndFollowerId(Long followeeId, Long followerId);
}
