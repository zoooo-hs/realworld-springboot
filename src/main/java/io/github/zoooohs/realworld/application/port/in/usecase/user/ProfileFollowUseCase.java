package io.github.zoooohs.realworld.application.port.in.usecase.user;

import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import io.github.zoooohs.realworld.domain.user.entity.UserId;

public interface ProfileFollowUseCase {
    ProfileResponse follow(UserId currentUserId, String username);

    ProfileResponse unfollow(UserId currentUserId, String username);
}
