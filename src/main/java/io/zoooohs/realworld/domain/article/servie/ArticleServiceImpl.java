package io.zoooohs.realworld.domain.article.servie;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.article.repository.ArticleRepository;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Transactional
    @Override
    public ArticleDto createArticle(ArticleDto article, UserDto.Auth authUser) {
        String slug = String.join("-", article.getTitle().split(" "));
        UserEntity author = UserEntity.builder()
                .id(authUser.getId())
                .name(authUser.getName())
                .bio(authUser.getBio())
                .image(authUser.getImage())
                .build();

        ArticleEntity articleEntity = ArticleEntity.builder()
                .slug(slug)
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .author(author)
                .build();
        articleEntity = articleRepository.save(articleEntity);
        return convertEntityToDto(articleEntity, false, 0L, false);
    }

    private ArticleDto convertEntityToDto(ArticleEntity entity, Boolean favorited, Long favoritesCount, Boolean following) {
        return ArticleDto.builder()
                .slug(entity.getSlug())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .body(entity.getBody())
                .author(ArticleDto.Author.builder()
                        .name(entity.getAuthor().getName())
                        .bio(entity.getAuthor().getBio())
                        .image(entity.getAuthor().getImage())
                        .following(following)
                        .build())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .favorited(favorited)
                .favoritesCount(favoritesCount)
                .build();
    }
}
