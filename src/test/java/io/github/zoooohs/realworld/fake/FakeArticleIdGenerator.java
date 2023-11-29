package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.domain.article.entity.ArticleId;
import io.github.zoooohs.realworld.domain.article.service.ArticleIdGenerator;

public class FakeArticleIdGenerator implements ArticleIdGenerator {
    private Long currentId;

    public FakeArticleIdGenerator(Long currentId) {
        this.currentId = currentId;
    }

    public Long getCurrentId() {
        return currentId;
    }

    @Override
    public ArticleId generate() {
        currentId++;
        return new ArticleId(currentId.toString());
    }
}
