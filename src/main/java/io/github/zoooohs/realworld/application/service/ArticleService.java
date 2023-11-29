package io.github.zoooohs.realworld.application.service;

import io.github.zoooohs.realworld.application.model.article.ArticleResponse;
import io.github.zoooohs.realworld.application.model.article.CreateArticleRequest;
import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import io.github.zoooohs.realworld.application.port.in.usecase.article.ArticleUseCase;
import io.github.zoooohs.realworld.application.port.in.usecase.user.ProfileUseCase;
import io.github.zoooohs.realworld.domain.article.entity.Article;
import io.github.zoooohs.realworld.domain.article.service.ArticleIdGenerator;
import io.github.zoooohs.realworld.domain.article.service.ArticleRepository;
import io.github.zoooohs.realworld.domain.user.entity.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArticleService implements ArticleUseCase {
    private final ArticleIdGenerator articleIdGenerator;
    private final ArticleRepository articleRepository;
    private final ProfileUseCase profileUseCase;


    @Override
    public ArticleResponse create(CreateArticleRequest request, UserId authorId) {
        Article article = Article.newArticle(request.title(), request.description(), request.body(), request.tagList(), authorId, articleIdGenerator);
        articleRepository.save(article);

        boolean favorited = article.isFavoritedBy(authorId);
        ProfileResponse author = profileUseCase.getProfile(authorId, authorId);
        return ArticleResponse
                .builder()
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .tagList(article.getTags())
                .favorited(favorited)
                .favoritesCount(article.getFavoritesCount())
                .createdAt(article.getCreatedAt().toString())
                .updatedAt(article.getUpdatedAt().toString())
                .author(author)
                .build();
    }
}
