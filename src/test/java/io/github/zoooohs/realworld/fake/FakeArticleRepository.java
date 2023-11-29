package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.domain.article.entity.Article;
import io.github.zoooohs.realworld.domain.article.entity.ArticleId;
import io.github.zoooohs.realworld.domain.article.service.ArticleRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeArticleRepository implements ArticleRepository {
    private final Map<ArticleId, Article> storage = new HashMap<>();

    @Override
    public void save(Article article) {
        storage.put(article.getId(), article);
    }
}
