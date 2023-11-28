package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.application.port.out.persistance.article.ArticleRepository;
import io.github.zoooohs.realworld.domain.model.article.Article;
import io.github.zoooohs.realworld.domain.model.article.ArticleId;

import java.util.HashMap;
import java.util.Map;

public class FakeArticleRepository implements ArticleRepository {
    private final Map<ArticleId, Article> storage = new HashMap<>();

    @Override
    public void save(Article article) {
        storage.put(article.getId(), article);
    }
}
