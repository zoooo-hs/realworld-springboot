package io.github.zoooohs.realworld.application.service;

import io.github.zoooohs.realworld.application.model.article.ArticleResponse;
import io.github.zoooohs.realworld.application.model.article.CreateArticleRequest;
import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import io.github.zoooohs.realworld.application.port.in.usecase.article.ArticleUseCase;
import io.github.zoooohs.realworld.application.port.in.usecase.user.ProfileUseCase;
import io.github.zoooohs.realworld.application.port.out.persistance.query.ProfileQueryHandler;
import io.github.zoooohs.realworld.domain.article.service.ArticleIdGenerator;
import io.github.zoooohs.realworld.domain.article.service.ArticleRepository;
import io.github.zoooohs.realworld.domain.user.entity.User;
import io.github.zoooohs.realworld.domain.user.entity.UserId;
import io.github.zoooohs.realworld.domain.user.service.PasswordManager;
import io.github.zoooohs.realworld.domain.user.service.UserIdGenerator;
import io.github.zoooohs.realworld.domain.user.service.UserRepository;
import io.github.zoooohs.realworld.fake.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ArticleServiceTest {

    @Test
    void create_article() {
        // GIVEN
        ProfileQueryHandler dummyProfileQueryHandler = getDummyProfileQueryHandler();
        ArticleIdGenerator articleIdGenerator = new FakeArticleIdGenerator(-1L);
        UserIdGenerator userIdGenerator = new FakeUserIdGenerator(-1L);
        UserRepository userRepository = new FakeUserRepository();
        PasswordManager passwordManager = new FakePasswordManager("fake");
        ArticleRepository articleRepository = new FakeArticleRepository();
        ProfileUseCase profileUseCase = new ProfileService(userRepository, dummyProfileQueryHandler);
        ArticleUseCase sut = new ArticleService(articleIdGenerator, articleRepository, profileUseCase);

        User author = User.newUser("author", "abc@def.zz", "password", passwordManager, userIdGenerator);
        UserId authorId = author.getId();
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

    private ProfileQueryHandler getDummyProfileQueryHandler() {
        return new ProfileQueryHandler() {
            @Override
            public Optional<ProfileResponse> findProfile(UserId currentUserId, String username) {
                return null;
            }
        };
    }

}