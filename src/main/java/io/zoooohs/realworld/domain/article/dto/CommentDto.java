package io.zoooohs.realworld.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @NotNull
    private String body;
    private ArticleDto.Author author;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleComment {
        CommentDto comment;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultipleComments {
        List<CommentDto> comments;
    }
}
