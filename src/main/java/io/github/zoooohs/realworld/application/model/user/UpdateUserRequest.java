package io.github.zoooohs.realworld.application.model.user;

import lombok.Builder;

@Builder
public record UpdateUserRequest(
        String email,
        String password,
        String username,
        String bio,
        String image
) { }
