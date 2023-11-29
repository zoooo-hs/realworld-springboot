package io.github.zoooohs.realworld.domain.article.entity;

import io.github.zoooohs.realworld.domain.article.service.ArticleIdGenerator;
import io.github.zoooohs.realworld.domain.user.entity.User;
import io.github.zoooohs.realworld.domain.user.entity.UserId;
import io.github.zoooohs.realworld.domain.user.service.PasswordManager;
import io.github.zoooohs.realworld.domain.user.service.UserIdGenerator;
import io.github.zoooohs.realworld.domain.user.service.UserRepository;
import io.github.zoooohs.realworld.fake.FakeArticleIdGenerator;
import io.github.zoooohs.realworld.fake.FakePasswordManager;
import io.github.zoooohs.realworld.fake.FakeUserIdGenerator;
import io.github.zoooohs.realworld.fake.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    private Article article;
    private ArticleId articleId;
    private ArticleIdGenerator articleIdGenerator;
    private UserId authorId;
    private UserIdGenerator userIdGenerator;
    private PasswordManager passwordManager;

    @BeforeEach
    void setUp() {
        articleIdGenerator = new FakeArticleIdGenerator(-1L);
        userIdGenerator = new FakeUserIdGenerator(-1L);
        passwordManager = new FakePasswordManager("fake");
        UserRepository userRepository = new FakeUserRepository();
        authorId = userIdGenerator.generate();
        User author = User.newUser("author", "abc@def.zz", "password", passwordManager, userIdGenerator);
        userRepository.save(author);
        articleId = articleIdGenerator.generate();
        article = Article.newArticle(articleId, "Hello World This is Title", "description", "this is body of the article", null, authorId);
    }

    @Test
    void get_slug_convert_title_and_append_id() {
        // GIVEN, WHEN
        String slug = article.getSlug();

        // THEN
        assertEquals("hello-world-this-is-title-"+articleId.id(), slug);
    }

    @Test
    void get_tags_when_tags_is_null_return_empty_list() {
        // GIVEN
        articleId = articleIdGenerator.generate();
        article = Article.newArticle(articleId, "Hello World This is Title", "description", "this is body of the article", null, authorId);

        // WHEN
        List<String> tags = article.getTags();

        // THEN
        assertAll(
                () -> assertNotNull(tags),
                () -> assertTrue(tags.isEmpty())
        );
    }

    @Test
    void isFavoritedBy_if_favorites_null_return_false() {
        article = Article.builder()
                .id(articleIdGenerator.generate())
                .title("title")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .favorites(null)
                .build();
        UserId userId = authorId;

        // WHEN
        boolean favoritedBy = article.isFavoritedBy(userId);

        // THEN
        assertFalse(favoritedBy);
    }
    
    @Test
    void isFavoritedBy_return_false_if_there_is_no_userId_in_favorites() {
        article = Article.builder()
                .id(articleIdGenerator.generate())
                .title("title")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .favorites(new ArrayList<>())
                .favorites(null)
                .build();
        UserId userId = authorId;

        // WHEN
        boolean favoritedBy = article.isFavoritedBy(userId);

        // THEN
        assertFalse(favoritedBy);
    }

    @Test
    void isFavoritedBy_return_true_if_there_is_userId_in_favorites() {
        UserId userId = authorId;
        article = Article.builder()
                .id(articleIdGenerator.generate())
                .title("title")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .favorites(Collections.singletonList(userId))
                .build();

        // WHEN
        boolean favoritedBy = article.isFavoritedBy(userId);

        // THEN
        assertTrue(favoritedBy);
    }

    @Test
    void getFavoritesCount_return_0_if_favorites_is_null() {
        article = Article.builder()
                .id(articleIdGenerator.generate())
                .title("title")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .favorites(null)
                .build();

        // WHEN
        long favoritesCount = article.getFavoritesCount();

        // THEN
        assertEquals(0, favoritesCount);
    }

    @Test
    void getFavoritesCount_return_favorites_length() {
        article = Article.builder()
                .id(articleIdGenerator.generate())
                .title("title")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .favorites(List.of(userIdGenerator.generate(), userIdGenerator.generate()))
                .build();

        // WHEN
        long favoritesCount = this.article.getFavoritesCount();

        // THEN
        assertEquals(2, favoritesCount);
    }
}