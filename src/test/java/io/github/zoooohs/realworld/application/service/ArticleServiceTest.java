package io.github.zoooohs.realworld.application.service;

import io.github.zoooohs.realworld.application.model.article.ArticleResponse;
import io.github.zoooohs.realworld.application.model.article.CreateArticleRequest;
import io.github.zoooohs.realworld.application.port.in.usecase.article.ArticleUseCase;
import io.github.zoooohs.realworld.application.port.in.usecase.user.ProfileUseCase;
import io.github.zoooohs.realworld.application.port.out.persistance.article.ArticleIdGenerator;
import io.github.zoooohs.realworld.application.port.out.persistance.article.ArticleRepository;
import io.github.zoooohs.realworld.application.port.out.persistance.user.UserIdGenerator;
import io.github.zoooohs.realworld.application.port.out.persistance.user.UserRepository;
import io.github.zoooohs.realworld.domain.model.user.User;
import io.github.zoooohs.realworld.domain.model.user.UserId;
import io.github.zoooohs.realworld.fake.FakeArticleIdGenerator;
import io.github.zoooohs.realworld.fake.FakeArticleRepository;
import io.github.zoooohs.realworld.fake.FakeUserIdGenerator;
import io.github.zoooohs.realworld.fake.FakeUserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleServiceTest {

    @Test
    void create_article() {
        // GIVEN
        ArticleIdGenerator articleIdGenerator = new FakeArticleIdGenerator(-1L);
        UserIdGenerator userIdGenerator = new FakeUserIdGenerator(-1L);
        UserRepository userRepository = new FakeUserRepository();
        ArticleRepository articleRepository = new FakeArticleRepository();
        ProfileUseCase profileUseCase = new ProfileService(userRepository);
        ArticleUseCase sut = new ArticleService(articleIdGenerator, articleRepository, profileUseCase);

        UserId authorId = userIdGenerator.generate();
        User author = new User(authorId, "abc@def.zz", "password", "author");
        userRepository.save(author);

        CreateArticleRequest createArticleRequest = new CreateArticleRequest("Hello World This is Title", "description", "this is body of the article", null);

        // WHEN
        ArticleResponse articleResponse = sut.create(createArticleRequest, authorId);


        // THEN
        assertAll(
                () -> assertEquals("Hello World This is Title", articleResponse.title()),
                () -> assertEquals("description", articleResponse.description()),
                () -> assertEquals("this is body of the article", articleResponse.body()),
                () -> assertTrue(articleResponse.tagList().isEmpty()),
                () -> assertTrue(articleResponse.slug().startsWith("hello-world-this-is-title-")),
                () -> assertEquals("author", articleResponse.author().username()),
                () -> assertNull(articleResponse.author().bio()),
                () -> assertNull(articleResponse.author().image()),
                () -> assertFalse(articleResponse.author().following())
        );
    }

}