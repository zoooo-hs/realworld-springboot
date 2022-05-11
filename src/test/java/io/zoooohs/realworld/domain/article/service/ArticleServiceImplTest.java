package io.zoooohs.realworld.domain.article.service;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.article.repository.ArticleRepository;
import io.zoooohs.realworld.domain.article.servie.ArticleServiceImpl;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {
    ArticleServiceImpl articleService;

    UserDto.Auth authUser;

    @Mock
    ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleService = new ArticleServiceImpl(articleRepository);
        authUser = UserDto.Auth.builder()
                .id(1L)
                .email("email@email.com")
                .name("testUser")
                .bio("bio")
                .image("photo-path")
                .build();
    }

    @Test
    void whenValidArticleForm_thenReturnArticle() {
        ArticleDto article = ArticleDto.builder()
                .title("article title")
                .description("description")
                .body("hi there")
                .tagList(List.of("tag1", "tag2"))
                .build();

        String expectedSlug = String.join("-", article.getTitle().split(" "));
        LocalDateTime beforeWrite = LocalDateTime.now();

        UserEntity author = UserEntity.builder()
                .id(authUser.getId())
                .name(authUser.getName())
                .bio(authUser.getBio())
                .image(authUser.getImage())
                .build();

        ArticleEntity expectedArticle = ArticleEntity.builder()
                .id(1L)
                .slug(expectedSlug)
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .author(author)
                .build();

        expectedArticle.setCreatedAt(LocalDateTime.now());
        expectedArticle.setUpdatedAt(LocalDateTime.now());


        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(expectedArticle);

        ArticleDto actual = articleService.createArticle(article, authUser);

        assertEquals(expectedSlug, actual.getSlug());
        assertEquals(authUser.getName(), actual.getAuthor().getName());
        assertTrue(beforeWrite.isBefore(actual.getCreatedAt()));
        assertTrue(beforeWrite.isBefore(actual.getUpdatedAt()));
        assertFalse(actual.getFavorited());
        assertEquals(0, actual.getFavoritesCount());
    }
}
