package io.github.zoooohs.realworld.application.model;

import lombok.Builder;

@Builder
public record UpdateUserRequest(
        String email,
        String password,
        String username,
        String bio,
        String image
) { }
