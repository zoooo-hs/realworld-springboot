package io.zoooohs.realworld.domain.profile.service;

import io.zoooohs.realworld.domain.profile.dto.ProfileDto;
import io.zoooohs.realworld.domain.user.dto.UserDto;

public interface ProfileService {
    ProfileDto getProfile(final String username, final UserDto.Auth authUser);

    ProfileDto followUser(final String name, final UserDto.Auth authUser);

    ProfileDto unfollowUser(final String name, final UserDto.Auth authUser);
}
