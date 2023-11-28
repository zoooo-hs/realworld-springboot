package io.github.zoooohs.realworld.application.model.article;

import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record ArticleResponse(
        String slug,
        String title,
        String description,
        String body,
        List<String> tagList,
        String createdAt,
        String updatedAt,
        boolean favorited,
        long favoritesCount,
        ProfileResponse author
) {
}
