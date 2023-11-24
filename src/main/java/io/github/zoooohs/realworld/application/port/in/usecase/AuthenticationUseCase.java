package io.github.zoooohs.realworld.application.port.in.usecase;

import io.github.zoooohs.realworld.application.model.AuthenticationRequest;
import io.github.zoooohs.realworld.application.model.RegistrationRequest;
import io.github.zoooohs.realworld.application.model.UpdateUserRequest;
import io.github.zoooohs.realworld.application.model.UsersResponse;
import io.github.zoooohs.realworld.domain.UserId;

public interface AuthenticationUseCase {
    UsersResponse registration(RegistrationRequest registrationRequest);
    UsersResponse authentication(AuthenticationRequest authenticationRequest);
    UsersResponse getCurrentUser(UserId currentUserId);
    UsersResponse updateCurrentUser(UserId currentUserId, UpdateUserRequest updateUserRequest);
}
