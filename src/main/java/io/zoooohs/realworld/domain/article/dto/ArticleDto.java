package io.zoooohs.realworld.domain.article.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ArticleDto {
    private String slug;

    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String body;
    private List<String> tagList;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean favorited;
    private Long favoritesCount;
    private Author author;

    // TODO: ProfileDTO 통합 필요
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Author {
        private String username;
        private String bio;
        private String image;
        private Boolean following;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleArticle<T> {
        private T article;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultipleArticle {
        private List<ArticleDto> articles;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        private String title;
        private String description;
        private String body;
    }
}
