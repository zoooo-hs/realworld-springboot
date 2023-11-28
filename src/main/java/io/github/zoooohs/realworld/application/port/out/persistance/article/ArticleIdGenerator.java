package io.github.zoooohs.realworld.application.port.out.persistance.article;

import io.github.zoooohs.realworld.domain.model.article.ArticleId;

public interface ArticleIdGenerator {
    ArticleId generate();
}
