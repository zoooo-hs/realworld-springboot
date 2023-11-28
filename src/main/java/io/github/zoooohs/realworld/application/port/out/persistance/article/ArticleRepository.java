package io.github.zoooohs.realworld.application.port.out.persistance.article;

import io.github.zoooohs.realworld.domain.model.article.Article;

public interface ArticleRepository {
    void save(Article article);
}
