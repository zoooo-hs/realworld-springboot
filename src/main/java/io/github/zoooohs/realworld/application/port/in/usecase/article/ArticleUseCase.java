package io.github.zoooohs.realworld.application.port.in.usecase.article;

import io.github.zoooohs.realworld.application.model.article.ArticleResponse;
import io.github.zoooohs.realworld.application.model.article.CreateArticleRequest;
import io.github.zoooohs.realworld.domain.user.entity.UserId;

public interface ArticleUseCase {
    ArticleResponse create(CreateArticleRequest request, UserId authorId);
}
