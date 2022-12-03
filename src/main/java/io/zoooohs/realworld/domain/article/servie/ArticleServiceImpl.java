package io.zoooohs.realworld.domain.article.servie;

import io.zoooohs.realworld.domain.article.dto.ArticleDto;
import io.zoooohs.realworld.domain.article.entity.ArticleEntity;
import io.zoooohs.realworld.domain.article.entity.FavoriteEntity;
import io.zoooohs.realworld.domain.article.model.ArticleQueryParam;
import io.zoooohs.realworld.domain.article.model.FeedParams;
import io.zoooohs.realworld.domain.article.repository.ArticleRepository;
import io.zoooohs.realworld.domain.article.repository.FavoriteRepository;
import io.zoooohs.realworld.domain.common.entity.BaseEntity;
import io.zoooohs.realworld.domain.profile.entity.FollowEntity;
import io.zoooohs.realworld.domain.profile.repository.FollowRepository;
import io.zoooohs.realworld.domain.profile.service.ProfileService;
import io.zoooohs.realworld.domain.tag.entity.ArticleTagRelationEntity;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.exception.AppException;
import io.zoooohs.realworld.exception.Error;
import io.zoooohs.realworld.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    // TODO: 1 svc, 1 repo 구조 갖추고 통합 서비스를 두는 것 고려
    private final ArticleRepository articleRepository;
    private final FollowRepository followRepository;
    private final FavoriteRepository favoriteRepository;

    private final ProfileService profileService;

    @Transactional
    @Override
    public ArticleDto createArticle(ArticleDto article, AuthUserDetails authUserDetails) {
        String slug = String.join("-", article.getTitle().split(" "));
        UserEntity author = UserEntity.builder()
                .id(authUserDetails.getId())
                .name(authUserDetails.getName())
                .build();

        ArticleEntity articleEntity = ArticleEntity.builder()
                .slug(slug)
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .author(author)
                .build();
        List<ArticleTagRelationEntity> tagList = new ArrayList<>();
        for (String tag: article.getTagList()) {
            tagList.add(ArticleTagRelationEntity.builder().article(articleEntity).tag(tag).build());
        }
        articleEntity.setTagList(tagList);

        articleEntity = articleRepository.save(articleEntity);
        return convertEntityToDto(articleEntity, false, 0L, false);
    }

    @Override
    public ArticleDto getArticle(String slug, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        Boolean following = profileService.getProfile(found.getAuthor().getName(), authUserDetails).getFollowing();
        List<FavoriteEntity> favorites = found.getFavoriteList();
        Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUserDetails.getId()));
        int favoriteCount = favorites.size();
        return convertEntityToDto(found, favorited, (long) favoriteCount, following);
    }

    private ArticleDto convertEntityToDto(ArticleEntity entity, Boolean favorited, Long favoritesCount, Boolean following) {
        // TODO: author, following -> profile service에서 get profile로 끝내기
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
                .tagList(entity.getTagList().stream().map(ArticleTagRelationEntity::getTag).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public ArticleDto updateArticle(String slug, ArticleDto.Update article, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).filter(entity -> entity.getAuthor().getId().equals(authUserDetails.getId())).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

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

        return getArticle(slug, authUserDetails);
    }

    @Transactional
    @Override
    public void deleteArticle(String slug, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).filter(entity -> entity.getAuthor().getId().equals(authUserDetails.getId())).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        articleRepository.delete(found);
    }

    @Override
    public List<ArticleDto> feedArticles(AuthUserDetails authUserDetails, FeedParams feedParams) {
        List<Long> feedAuthorIds = followRepository.findByFollowerId(authUserDetails.getId()).stream().map(FollowEntity::getFollowee).map(BaseEntity::getId).collect(Collectors.toList());
        return articleRepository.findByAuthorIdInOrderByCreatedAtDesc(feedAuthorIds, PageRequest.of(feedParams.getOffset(), feedParams.getLimit())).stream().map(entity -> {
            List<FavoriteEntity> favorites = entity.getFavoriteList();
            Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUserDetails.getId()));
            int favoriteCount = favorites.size();
            return convertEntityToDto(entity, favorited, (long) favoriteCount, true);
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ArticleDto favoriteArticle(String slug, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));

        favoriteRepository.findByArticleIdAndUserId(found.getId(), authUserDetails.getId())
                .ifPresent(favoriteEntity -> { throw new AppException(Error.ALREADY_FAVORITED_ARTICLE);});

        FavoriteEntity favorite = FavoriteEntity.builder()
                .article(found)
                .user(UserEntity.builder().id(authUserDetails.getId()).build())
                .build();
        favoriteRepository.save(favorite);

        return getArticle(slug, authUserDetails);
    }

    @Transactional
    @Override
    public ArticleDto unfavoriteArticle(String slug, AuthUserDetails authUserDetails) {
        ArticleEntity found = articleRepository.findBySlug(slug).orElseThrow(() -> new AppException(Error.ARTICLE_NOT_FOUND));
        FavoriteEntity favorite = found.getFavoriteList().stream()
                .filter(favoriteEntity -> favoriteEntity.getArticle().getId().equals(found.getId())
                        && favoriteEntity.getUser().getId().equals(authUserDetails.getId())).findAny()
                .orElseThrow(() -> new AppException(Error.FAVORITE_NOT_FOUND));
        found.getFavoriteList().remove(favorite); // cascade REMOVE
        return getArticle(slug, authUserDetails);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArticleDto> listArticle(ArticleQueryParam articleQueryParam, AuthUserDetails authUserDetails) {
        Pageable pageable = null;
        if (articleQueryParam.getOffset() != null) {
            pageable = PageRequest.of(articleQueryParam.getOffset(), articleQueryParam.getLimit());
        }

        List<ArticleEntity> articleEntities;
        if (articleQueryParam.getTag() != null) {
            articleEntities = articleRepository.findByTag(articleQueryParam.getTag(), pageable);
        } else if  (articleQueryParam.getAuthor() != null) {
            articleEntities = articleRepository.findByAuthorName(articleQueryParam.getAuthor(), pageable);
        } else if (articleQueryParam.getFavorited() != null) {
            articleEntities = articleRepository.findByFavoritedUsername(articleQueryParam.getFavorited(), pageable);
        } else {
            articleEntities = articleRepository.findListByPaging(pageable);
        }

        return convertToArticleList(articleEntities, authUserDetails);
    }

    private List<ArticleDto> convertToArticleList(List<ArticleEntity> articleEntities, AuthUserDetails authUserDetails) {
        List<Long> authorIds = articleEntities.stream().map(ArticleEntity::getAuthor).map(BaseEntity::getId).collect(Collectors.toList());
        List<Long> followeeIds = followRepository.findByFollowerIdAndFolloweeIdIn(authUserDetails.getId(), authorIds).stream().map(FollowEntity::getFollowee).map(BaseEntity::getId).collect(Collectors.toList());

        return articleEntities.stream().map(entity -> {
            List<FavoriteEntity> favorites = entity.getFavoriteList();
            Boolean favorited = favorites.stream().anyMatch(favoriteEntity -> favoriteEntity.getUser().getId().equals(authUserDetails.getId()));
            int favoriteCount = favorites.size();
            Boolean following = followeeIds.stream().anyMatch(followeeId -> followeeId.equals(entity.getAuthor().getId()));
            return convertEntityToDto(entity, favorited, (long) favoriteCount, following);
        }).collect(Collectors.toList());
    }
}
