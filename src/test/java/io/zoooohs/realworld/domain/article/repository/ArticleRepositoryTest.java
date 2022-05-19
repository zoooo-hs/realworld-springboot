package io.zoooohs.realworld.domain.article.repository;

import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.article.entity.FavoriteEntity;
import io.zoooohs.realworld.domain.tag.entity.ArticleTagRelationEntity;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
public class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;
    private UserEntity user1;
    private UserEntity user2;
    private ArticleEntity article1;

    // TODO unique slug -> then slug need to be random?

    @BeforeEach
    void setUp() {
        user1 = UserEntity.builder().name("username").email("test@test.com").password("password").bio("").build();
        user2 = UserEntity.builder().name("username2").email("tes2t@test.com").password("password").bio("").build();

        userRepository.saveAll(List.of(user1, user2));

        article1 = ArticleEntity.builder()
                .title("title")
                .slug("slug-1")
                .description("desc")
                .body("body")
                .author(user1).build();

        articleRepository.save(article1);
    }

    @Test
    void whenListOfAuthorId_thenReturnArticlesHaveAuthorIdAndOrderByCreatedAtDesc() {
        ArticleEntity article2 = ArticleEntity.builder()
                .title("title")
                .slug("slug-2")
                .description("desc")
                .body("body")
                .author(user2).build();

        ArticleEntity article3 = ArticleEntity.builder()
                .title("title")
                .slug("slug-3")
                .description("desc")
                .body("body")
                .author(user2).build();
        articleRepository.saveAll(List.of(article2, article3));

        List<Long> ids = List.of(user1.getId(), user2.getId());

        List<ArticleEntity> actual = articleRepository.findByAuthorIdInOrderByCreatedAtDesc(ids, PageRequest.of(0, 3));

        assertEquals(3, actual.size());
        assertTrue(actual.get(0).getCreatedAt().isAfter(actual.get(1).getCreatedAt()));
        assertTrue(actual.get(1).getCreatedAt().isAfter(actual.get(2).getCreatedAt()));
    }

    @Test
    void whenCreateArticleWithTagList_thenCascadeToTagRelationEntity() {
        article1.setTagList(List.of(ArticleTagRelationEntity.builder().article(article1).tag("tag1").build(),ArticleTagRelationEntity.builder().article(article1).tag("tag2").build()));
        articleRepository.flush();

        Optional<ArticleEntity> maybeArticle = articleRepository.findById(article1.getId());

        assertTrue(maybeArticle.isPresent());
        assertNotNull(maybeArticle.get().getTagList().get(0).getId());
        assertNotNull(maybeArticle.get().getTagList().get(1).getId());
    }

    @Test
    void whenThereAreArticlesHaveTag_thenReturnArticles() {
        article1.setTagList(List.of(ArticleTagRelationEntity.builder().article(article1).tag("tag1").build(),ArticleTagRelationEntity.builder().article(article1).tag("tag2").build()));
        articleRepository.flush();
//        articleRepository.save(article1);

        List<ArticleEntity> actual = articleRepository.findByTag("tag1", null);

        assertEquals(1, actual.size());
        assertTrue(actual.get(0).getTagList().stream().anyMatch(articleTagRelationEntity -> articleTagRelationEntity.getTag().equals("tag1")));
    }

    @Test
    void whenThereAreArticlesHasFavorite_thenReturnArticlesByFavoriteUsername() {
        FavoriteEntity favorite = FavoriteEntity.builder().article(article1).user(user1).build();
        article1.setFavoriteList(List.of(favorite));
        articleRepository.flush();


        List<ArticleEntity> actual = articleRepository.findByFavoritedUsername(user1.getName(), null);

        assertEquals(1, actual.size());
        assertTrue(actual.get(0).getFavoriteList().stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(user1.getId())));
    }

    @Test
    void whenThereAreArticles_thenReturnArticlesByAuthorName() {
        List<ArticleEntity> actual = articleRepository.findByAuthorName(user1.getName(), null);

        assertEquals(1, actual.size());
        assertEquals(actual.get(0).getAuthor().getName(), user1.getName());
    }
}
