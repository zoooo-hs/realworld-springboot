package io.github.zoooohs.realworld.application.port.out.persistance.query;

import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import io.github.zoooohs.realworld.domain.user.entity.UserId;

import java.util.Optional;

public interface ProfileQueryHandler {
    Optional<ProfileResponse> findProfile(UserId currentUserId, String username);
}
