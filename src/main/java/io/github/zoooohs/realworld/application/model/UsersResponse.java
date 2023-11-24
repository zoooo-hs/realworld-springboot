package io.github.zoooohs.realworld.application.model;

import lombok.Builder;

@Builder
public record UsersResponse(String email, String token, String username, String bio, String image) {}
