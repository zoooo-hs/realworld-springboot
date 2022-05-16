package io.zoooohs.realworld.domain.article.repository;

import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    @EntityGraph("fetch-author")
    Optional<ArticleEntity> findBySlug(String slug);

    @EntityGraph("fetch-author")
    List<ArticleEntity> findByAuthorIdInOrderByCreatedAtDesc(List<Long> feedAuthorIds, Pageable pageable);
}
