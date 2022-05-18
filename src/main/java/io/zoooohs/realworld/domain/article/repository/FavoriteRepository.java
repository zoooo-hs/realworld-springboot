package io.zoooohs.realworld.domain.article.repository;

import io.zoooohs.realworld.domain.article.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    Optional<FavoriteEntity> findByArticleIdAndUserId(Long articleId, Long userId);
}
