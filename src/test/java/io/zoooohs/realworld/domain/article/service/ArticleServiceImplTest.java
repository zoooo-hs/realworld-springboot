package io.zoooohs.realworld.domain.article.service;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.article.repository.ArticleRepository;
import io.zoooohs.realworld.domain.article.servie.ArticleServiceImpl;
import io.zoooohs.realworld.domain.profile.dto.ProfileDto;
import io.zoooohs.realworld.domain.profile.entity.FollowEntity;
import io.zoooohs.realworld.domain.profile.repository.FollowRepository;
import io.zoooohs.realworld.domain.profile.service.ProfileService;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {
    ArticleServiceImpl articleService;

    UserDto.Auth authUser;

    @Mock
    ArticleRepository articleRepository;

    @Mock
    ProfileService profileService;

    @Mock
    FollowRepository followRepository;

    private ArticleDto article;
    private String expectedSlug;
    private UserEntity author;
    private ArticleEntity expectedArticle;
    private LocalDateTime beforeWrite;

    @BeforeEach
    void setUp() {
        articleService = new ArticleServiceImpl(articleRepository, followRepository, profileService);
        authUser = UserDto.Auth.builder()
                .id(1L)
                .email("email@email.com")
                .name("testUser")
                .bio("bio")
                .image("photo-path")
                .build();
        article = ArticleDto.builder()
                .title("article title")
                .description("description")
                .body("hi there")
                .tagList(List.of("tag1", "tag2"))
                .build();

        expectedSlug = String.join("-", article.getTitle().split(" "));

        author = UserEntity.builder()
                .id(authUser.getId())
                .name(authUser.getName())
                .bio(authUser.getBio())
                .image(authUser.getImage())
                .build();

        expectedArticle = ArticleEntity.builder()
                .id(1L)
                .slug(expectedSlug)
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .author(author)
                .build();

        beforeWrite = LocalDateTime.now();

        expectedArticle.setCreatedAt(LocalDateTime.now());
        expectedArticle.setUpdatedAt(expectedArticle.getCreatedAt());
    }

    @Test
    void whenValidArticleForm_thenReturnArticle() {
        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(expectedArticle);

        ArticleDto actual = articleService.createArticle(article, authUser);

        assertEquals(expectedSlug, actual.getSlug());
        assertEquals(authUser.getName(), actual.getAuthor().getName());
        assertTrue(beforeWrite.isBefore(actual.getCreatedAt()));
        assertTrue(beforeWrite.isBefore(actual.getUpdatedAt()));
        assertFalse(actual.getFavorited());
        assertEquals(0, actual.getFavoritesCount());
    }

    @Test
    void whenThereIsArticleWithSlug_thenReturnSingleArticle() {
        String slug = "article-title";

        when(articleRepository.findBySlug(eq(slug))).thenReturn(Optional.ofNullable(expectedArticle));
        when(profileService.getProfile(eq(author.getName()), any(UserDto.Auth.class))).thenReturn(ProfileDto.builder().following(false).build());

        ArticleDto actual = articleService.getArticle(slug, authUser);

        assertEquals(slug, actual.getSlug());
        assertEquals("article title", actual.getTitle());
    }

    @Test
    void whenUpdateArticleWithNewTitle_thenReturnUpdatedSingleArticleWithUpdatedTitleAndSlug() {
        String slug = "article-title";
        ArticleDto.Update updateArticle = ArticleDto.Update.builder().title("new title").build();

        when(articleRepository.findBySlug(eq(slug))).thenReturn(Optional.ofNullable(expectedArticle));
        when(profileService.getProfile(eq(author.getName()), any(UserDto.Auth.class))).thenReturn(ProfileDto.builder().following(false).build());

        ArticleDto actual = articleService.updateArticle(slug, updateArticle, authUser);

        assertEquals(updateArticle.getTitle(), actual.getTitle());
        assertEquals("new-title", actual.getSlug());
    }

    @Test
    void whenDeleteValidSlug_thenReturnVoid() {
        String slug = "article-title";
        when(articleRepository.findBySlug(eq(slug))).thenReturn(Optional.ofNullable(expectedArticle));

        articleService.deleteArticle(slug, authUser);

        verify(articleRepository, times(1)).delete(any(ArticleEntity.class));
    }

    @Test
    void whenValidUserFeed_thenReturnMultipleArticle() {
        when(followRepository.findByFollowerId(eq(authUser.getId()))).thenReturn(List.of(FollowEntity.builder().followee(author).build()));
        when(articleRepository.findByAuthorIdIn(anyList())).thenReturn(List.of(expectedArticle));

        List<ArticleDto> actual = articleService.feedArticles(authUser);

        assertEquals(1, actual.size());
        assertTrue(actual.get(0).getAuthor().getFollowing());
    }
}
