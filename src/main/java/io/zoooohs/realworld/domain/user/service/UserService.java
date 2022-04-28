package io.zoooohs.realworld.domain.user.service;

import io.zoooohs.realworld.domain.user.dto.UserDto;

public interface UserService {
    UserDto registration(final UserDto.Registration registration);
}
