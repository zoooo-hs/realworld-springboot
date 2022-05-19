package io.zoooohs.realworld.domain.article.repository;

import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    @EntityGraph("fetch-author-tagList")
    @Query("SELECT a FROM ArticleEntity a LEFT JOIN FavoriteEntity f ON f.article.id = a.id WHERE a.slug = :slug")
    Optional<ArticleEntity> findBySlug(@Param("slug") String slug);

    @EntityGraph("fetch-author-tagList")
    @Query("SELECT a FROM ArticleEntity a LEFT JOIN FavoriteEntity f ON f.article.id = a.id WHERE a.author.id IN :ids ORDER BY a.createdAt DESC")
    List<ArticleEntity> findByAuthorIdInOrderByCreatedAtDesc(@Param("ids") List<Long> ids, Pageable pageable);

    @EntityGraph("fetch-author-tagList")
    @Query("SELECT a FROM ArticleEntity a LEFT JOIN FavoriteEntity f ON f.article.id = a.id ORDER BY a.createdAt DESC")
    List<ArticleEntity> findListByPaging(Pageable pageable);

    @EntityGraph("fetch-author-tagList")
    @Query("SELECT a FROM ArticleEntity a LEFT JOIN FavoriteEntity f ON f.article.id = a.id WHERE a.author.name = :name ORDER BY a.createdAt DESC")
    List<ArticleEntity> findByAuthorName(@Param("name") String name, Pageable pageable);

    @EntityGraph("fetch-author-tagList")
    @Query("SELECT a FROM ArticleEntity a JOIN ArticleTagRelationEntity t ON t.article.id = a.id LEFT JOIN FavoriteEntity f ON f.article.id = a.id WHERE t.tag = :tag ORDER BY a.createdAt DESC")
    List<ArticleEntity> findByTag(@Param("tag") String tag, Pageable pageable);

    @EntityGraph("fetch-author-tagList")
    @Query("SELECT a FROM ArticleEntity a LEFT JOIN FavoriteEntity f ON f.article.id = a.id WHERE f.user.name = :name ORDER BY a.createdAt DESC")
    List<ArticleEntity> findByFavoritedUsername(@Param("name") String name, Pageable pageable);
}
