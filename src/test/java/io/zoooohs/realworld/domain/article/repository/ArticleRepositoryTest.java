package io.zoooohs.realworld.domain.article.repository;

import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ArticleRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    // TODO unique slug -> then slug need to be random?

    @Test
    void whenListOfAuthorId_thenReturnArticlesHaveAuthorId() {
        UserEntity user1 = UserEntity.builder().name("username").email("test@test.com").password("password").bio("").build();
        UserEntity user2 = UserEntity.builder().name("username2").email("tes2t@test.com").password("password").bio("").build();
        userRepository.saveAll(List.of(user1, user2));

        ArticleEntity article1 = ArticleEntity.builder()
                .title("title")
                .slug("slug-1")
                .description("desc")
                .body("body")
                .author(user1).build();

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
        articleRepository.saveAll(List.of(article1, article2, article3));

        List<Long> ids = List.of(user1.getId(), user2.getId());

        List<ArticleEntity> actual = articleRepository.findByAuthorIdIn(ids);

        assertEquals(3, actual.size());
    }
}
