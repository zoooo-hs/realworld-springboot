package io.github.zoooohs.realworld.application.model.article;

import java.util.List;

public record CreateArticleRequest(String title, String description, String body, List<String> tagList) {
}
