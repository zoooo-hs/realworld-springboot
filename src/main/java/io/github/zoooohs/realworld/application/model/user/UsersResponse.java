package io.github.zoooohs.realworld.application.model.user;

import lombok.Builder;

@Builder
public record UsersResponse(String email, String token, String username, String bio, String image) {}
