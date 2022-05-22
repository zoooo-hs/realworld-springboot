package io.zoooohs.realworld.domain.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zoooohs.realworld.configuration.WithAuthUser;
import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.dto.CommentDto;
import io.zoooohs.realworld.domain.article.servie.ArticleService;
import io.zoooohs.realworld.domain.article.servie.CommentService;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.security.JWTAuthFilter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ArticlesController.class)
public class ArticlesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JWTAuthFilter jwtAuthFilter;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    ArticleService articleService;

    @MockBean
    CommentService commentService;

    private ArticleDto.Author author;
    private ArticleDto article;
    private ArticleDto.SingleArticle singleArticle;
    private String slug;


    @BeforeEach
    void setUp() {
        author =  ArticleDto.Author.builder().name("username").build();
        article = ArticleDto.builder()
                .title("article title")
                .description("description")
                .body("hi there")
                .tagList(List.of("tag1", "tag2"))
                .author(author)
                .build();
        singleArticle = ArticleDto.SingleArticle.builder().article(article).build();
        slug = "article-title";
    }

    @Test
    @WithAuthUser
    void whenValidArticleForm_thenReturnArticle() throws Exception {
        when(articleService.createArticle(any(ArticleDto.class), any(UserDto.Auth.class))).thenReturn(article);

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(singleArticle))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article", Matchers.notNullValue(ArticleDto.class)));
    }

    @Test
    @WithAuthUser
    void whenThereIsArticleWithSlug_thenReturnSingleArticle() throws Exception {
        when(articleService.getArticle(eq(slug), any(UserDto.Auth.class))).then((Answer<ArticleDto>) invocation ->
                ArticleDto.builder()
                        .slug(slug)
                        .title("article title")
                        .description("description")
                        .body("hi there")
                        .tagList(List.of("tag1", "tag2"))
                        .build()
        );

        mockMvc.perform(get("/articles/" + slug))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue(ArticleDto.SingleArticle.class)))
                .andExpect(jsonPath("$.article", Matchers.notNullValue(ArticleDto.class)))
                .andExpect(jsonPath("$.article.title", Matchers.is(article.getTitle())))
                .andExpect(jsonPath("$.article.slug", Matchers.is(slug)));
    }

    @Test
    @WithAuthUser
    void whenPUTValidArticleForm_thenReturnUpdatedSingleArticle() throws Exception {
        when(articleService.updateArticle(anyString(), any(ArticleDto.Update.class), any(UserDto.Auth.class))).thenReturn(article);

        mockMvc.perform(put("/articles/hello-world")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(singleArticle))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article", Matchers.notNullValue(ArticleDto.class)));
    }

    @Test
    @WithAuthUser
    void whenDeleteValidSlug_thenReturnVoid() throws Exception {
        mockMvc.perform(delete("/articles/hello-world"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAuthUser
    void whenValidUserFeed_thenReturnMultipleArticle() throws Exception {
        article = ArticleDto.builder()
                .title("article title")
                .description("description")
                .body("hi there")
                .tagList(List.of("tag1", "tag2"))
                .author(ArticleDto.Author.builder().following(true).build())
                .build();

        when(articleService.feedArticles(any(UserDto.Auth.class), any())).thenReturn(List.of(article));

        mockMvc.perform(get("/articles/feed")
                        .param("limit", "1")
                        .param("offset", "0")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.articles[0]", Matchers.notNullValue(ArticleDto.class)))
                .andExpect(jsonPath("$.articles[0].author.following", Matchers.is(true)));
    }

    @Test
    @WithAuthUser
    void whenPageIsNotValid_thenReturn() throws Exception {
        mockMvc.perform(get("/articles/feed")
                        .param("offset", "0")
                )
                .andExpect(status().isUnprocessableEntity());
        mockMvc.perform(get("/articles/feed")
                        .param("limit", "1")
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithAuthUser
    void whenFavoriteArticle_thenReturnArticleWithUpdatedFavorite() throws Exception {
        article.setFavorited(true);
        when(articleService.favoriteArticle(eq("some-slug"), any(UserDto.Auth.class))).thenReturn(article);

        mockMvc.perform(post("/articles/some-slug/favorite")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article", Matchers.notNullValue(ArticleDto.class)))
                .andExpect(jsonPath("$.article.favorited", Matchers.is(true)));
    }

    @Test
    @WithAuthUser
    void whenUnfavoriteArticle_thenReturnArticleWithUpdatedFavorite() throws Exception {
        article.setFavorited(false);
        when(articleService.unfavoriteArticle(eq("some-slug"), any(UserDto.Auth.class))).thenReturn(article);

        mockMvc.perform(delete("/articles/some-slug/favorite")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article", Matchers.notNullValue(ArticleDto.class)))
                .andExpect(jsonPath("$.article.favorited", Matchers.is(false)));
    }

    @ParameterizedTest
    @MethodSource("articleQueries")
    @WithAuthUser
    void whenQueryArticles_thenReturnArticles(String key, String value) throws Exception {
        when(articleService.listArticle(any(), any())).thenReturn(List.of(article));

        ResultActions result =  mockMvc.perform(get("/articles")
                        .param("limit", "1")
                        .param("offset", "0")
                        .param(key, value)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.articles[0]", Matchers.notNullValue(ArticleDto.class)));

        if (key.equals("tag")) {
            result.andExpect(jsonPath("$.articles[0].tagList[0]", Matchers.is(value)));
        } else if (key.equals("author")) {
            result.andExpect(jsonPath("$.articles[0].author.username", Matchers.is(value)));
        }
    }

    public static Stream<Arguments> articleQueries() {
        return Stream.of(
                Arguments.of("tag", "tag1"),
                Arguments.of("author", "username"),
                Arguments.of("favorited", "username")
        );
    }

    @Test
    @WithAuthUser
    void whenPostCommentForArticleSlug_thenReturnComment() throws Exception {
        CommentDto comment = CommentDto.builder()
                .body("hello world")
                .build();

        CommentDto result = CommentDto.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .author(author)
                .body("hello world")
                .build();


        when(commentService.addCommentsToAnArticle(anyString(), any(), any(UserDto.Auth.class))).thenReturn(result);

        mockMvc.perform(post("/articles/some-slug/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment", Matchers.notNullValue(CommentDto.class)));
    }

    @Test
    @WithAuthUser
    void whenDeleteCommentIdArticleSlug_thenDelete() throws Exception {
        mockMvc.perform(delete("/articles/some-slug/comments/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAuthUser
    void whenGetArticleCommentsBySlug_thenReturnComments() throws Exception {
        when(commentService.getCommentsBySlug(anyString(), any(UserDto.Auth.class)))
                .thenReturn(List.of(CommentDto.builder().build()));

        mockMvc.perform(get("/articles/some-slug/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0]", Matchers.notNullValue(CommentDto.class)));
    }
}
