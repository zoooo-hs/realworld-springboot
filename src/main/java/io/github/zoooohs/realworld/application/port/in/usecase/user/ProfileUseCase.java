package io.github.zoooohs.realworld.application.port.in.usecase.user;

import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import io.github.zoooohs.realworld.domain.user.entity.UserId;

public interface ProfileUseCase {
    ProfileResponse getProfile(UserId currentUserId, String username);
    ProfileResponse getProfile(UserId currentUserId, UserId userId);
}
