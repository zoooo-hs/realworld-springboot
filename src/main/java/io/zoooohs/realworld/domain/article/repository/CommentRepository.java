package io.zoooohs.realworld.domain.article.repository;

import io.zoooohs.realworld.domain.article.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
