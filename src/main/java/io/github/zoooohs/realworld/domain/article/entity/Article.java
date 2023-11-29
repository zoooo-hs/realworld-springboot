package io.github.zoooohs.realworld.domain.article.entity;

import io.github.zoooohs.realworld.domain.article.service.ArticleIdGenerator;
import io.github.zoooohs.realworld.domain.user.entity.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {
    private final ArticleId id;
    private String title;
    private String description;
    private String body;
    private List<String> tags;
    private List<UserId> favorites;
    private UserId author;
    private Instant createdAt;
    private Instant updatedAt;

    public static Article newArticle(String title, String description, String body, List<String> tags, UserId authorId, ArticleIdGenerator articleIdGenerator) {
        ArticleId id = articleIdGenerator.generate();
        Instant now = Instant.now();
        return Article.builder()
                .id(id)
                .title(title)
                .description(description)
                .body(body)
                .tags(tags)
                .createdAt(now)
                .updatedAt(now)
                .author(authorId)
                .build();
    }

    public String getSlug() {
        String convertedTitle = title.toLowerCase().replace(" ", "-");
        return convertedTitle + "-" + id.id();
    }

    public List<String> getTags() {
        if (tags == null) return Collections.emptyList();
        return tags;
    }

    public boolean isFavoritedBy(UserId userId) {
        if (favorites == null) {
            return false;
        }
        return favorites.contains(userId);
    }

    public long getFavoritesCount() {
        if (favorites == null) {
            return 0;
        }
        return favorites.size();
    }
}
