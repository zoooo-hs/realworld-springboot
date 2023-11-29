package io.github.zoooohs.realworld.domain.article.service;

import io.github.zoooohs.realworld.domain.article.entity.Article;

public interface ArticleRepository {
    void save(Article article);
}
