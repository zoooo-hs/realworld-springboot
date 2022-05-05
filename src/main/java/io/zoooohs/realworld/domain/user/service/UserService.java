package io.zoooohs.realworld.domain.user.service;

import io.zoooohs.realworld.domain.user.dto.UserDto;

public interface UserService {
    UserDto registration(final UserDto.Registration registration);

    UserDto login(final UserDto.Login login);

    UserDto currentUser(final UserDto.Auth authUser);

    UserDto update(final UserDto.Update update, final UserDto.Auth authUser);
}
