package io.zoooohs.realworld.domain.article.service;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.dto.CommentDto;
import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.article.repository.ArticleRepository;
import io.zoooohs.realworld.domain.article.repository.CommentRepository;
import io.zoooohs.realworld.domain.article.servie.CommentServiceImpl;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    CommentServiceImpl commentService;

    @Mock
    ArticleRepository articleRepository;
    @Mock
    CommentRepository commentRepository;

    UserDto.Auth authUser;
    ArticleDto article;
    String expectedSlug;

    UserEntity author;
    ArticleEntity expectedArticle;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceImpl(articleRepository, commentRepository);

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
                .favoritesCount(0L)
                .favorited(false)
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
    }

    @Test
    void whenCommentForArticleSlug_thenReturnComment() {
        String slug = "hello";
        CommentDto commentDto = CommentDto.builder().body("body").build();

        when(articleRepository.findBySlug(eq(slug))).thenReturn(Optional.of(expectedArticle));

        CommentDto actual = commentService.addCommentsToAnArticle(slug, commentDto, authUser);

        assertEquals(commentDto.getBody(), actual.getBody());
        assertEquals(authUser.getName(), actual.getAuthor().getName());
    }
}
