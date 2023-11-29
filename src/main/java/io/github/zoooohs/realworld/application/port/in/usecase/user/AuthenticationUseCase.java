package io.github.zoooohs.realworld.application.port.in.usecase.user;

import io.github.zoooohs.realworld.application.model.user.AuthenticationRequest;
import io.github.zoooohs.realworld.application.model.user.RegistrationRequest;
import io.github.zoooohs.realworld.application.model.user.UpdateUserRequest;
import io.github.zoooohs.realworld.application.model.user.UsersResponse;
import io.github.zoooohs.realworld.domain.user.entity.UserId;

public interface AuthenticationUseCase {
    UsersResponse registration(RegistrationRequest registrationRequest);
    UsersResponse authentication(AuthenticationRequest authenticationRequest);
    UsersResponse getCurrentUser(UserId currentUserId);
    UsersResponse updateCurrentUser(UserId currentUserId, UpdateUserRequest updateUserRequest);
}
