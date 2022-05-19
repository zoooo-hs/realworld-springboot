package io.zoooohs.realworld.domain.tag.repository;

import io.zoooohs.realworld.domain.tag.entity.ArticleTagRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<ArticleTagRelationEntity, Long> {
}
