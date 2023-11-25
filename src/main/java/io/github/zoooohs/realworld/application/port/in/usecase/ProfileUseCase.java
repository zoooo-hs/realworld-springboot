package io.github.zoooohs.realworld.application.port.in.usecase;

import io.github.zoooohs.realworld.application.model.ProfileResponse;
import io.github.zoooohs.realworld.domain.model.UserId;

public interface ProfileUseCase {
    ProfileResponse getProfile(UserId currentUserId, String username);

    ProfileResponse follow(UserId currentUserId, String username);

    ProfileResponse unfollow(UserId currentUserId, String username);
}
