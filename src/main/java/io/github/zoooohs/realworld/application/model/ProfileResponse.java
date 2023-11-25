package io.github.zoooohs.realworld.application.model;

import lombok.Builder;

@Builder
public record ProfileResponse(String username, String bio, String image, boolean following) {
}
