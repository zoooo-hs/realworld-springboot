package io.zoooohs.realworld.domain.article.servie;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.article.repository.ArticleRepository;
import io.zoooohs.realworld.domain.common.entity.BaseEntity;
import io.zoooohs.realworld.domain.profile.entity.FollowEntity;
import io.zoooohs.realworld.domain.profile.repository.FollowRepository;
import io.zoooohs.realworld.domain.profile.service.ProfileService;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.exception.AppException;
import io.zoooohs.realworld.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final FollowRepository followRepository;

    private final ProfileService profileService;

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

    @Override
    public ArticleDto getArticle(String slug, UserDto.Auth authUser) {
        ArticleEntity found = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        Boolean following = profileService.getProfile(found.getAuthor().getName(), authUser).getFollowing();
        return convertEntityToDto(found, false, 0L, following);
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

    @Transactional
    @Override
    public ArticleDto updateArticle(String slug, ArticleDto.Update article, UserDto.Auth authUser) {
        ArticleEntity found = articleRepository.findBySlug(slug).filter(entity -> entity.getAuthor().getId().equals(authUser.getId())).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        if (article.getTitle() != null) {
            String newSlug = String.join("-", article.getTitle().split(" "));
            found.setTitle(article.getTitle());
            found.setSlug(newSlug);
        }

        if (article.getDescription() != null) {
            found.setDescription(article.getDescription());
        }

        if (article.getBody() != null) {
            found.setBody(article.getBody());
        }

        articleRepository.save(found);

        Boolean following = profileService.getProfile(found.getAuthor().getName(), authUser).getFollowing();
        return convertEntityToDto(found, false, 0L, following);
    }

    @Transactional
    @Override
    public void deleteArticle(String slug, UserDto.Auth authUser) {
        ArticleEntity found = articleRepository.findBySlug(slug).filter(entity -> entity.getAuthor().getId().equals(authUser.getId())).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        articleRepository.delete(found);
    }

    @Override
    public List<ArticleDto> feedArticles(UserDto.Auth authUser) {
        List<Long> feedAuthorIds = followRepository.findByFollowerId(authUser.getId()).stream().map(FollowEntity::getFollowee).map(BaseEntity::getId).collect(Collectors.toList());
        return articleRepository.findByAuthorIdIn(feedAuthorIds).stream().map(entity -> convertEntityToDto(entity, false, 0L, true)).collect(Collectors.toList());
    }
}
