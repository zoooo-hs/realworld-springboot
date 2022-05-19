package io.zoooohs.realworld.domain.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class TagDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagList {
        List<String> tags;
    }
}
