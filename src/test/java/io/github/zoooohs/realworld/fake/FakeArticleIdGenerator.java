package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.application.port.out.persistance.article.ArticleIdGenerator;
import io.github.zoooohs.realworld.domain.model.article.ArticleId;

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
