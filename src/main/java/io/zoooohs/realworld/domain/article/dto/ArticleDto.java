package io.zoooohs.realworld.domain.article.dto;

import io.zoooohs.realworld.domain.profile.dto.ProfileDto;
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
    private ProfileDto author;

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
