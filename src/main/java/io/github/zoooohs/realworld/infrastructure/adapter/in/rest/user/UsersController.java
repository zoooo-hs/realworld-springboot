package io.github.zoooohs.realworld.infrastructure.adapter.in.rest.user;

import io.github.zoooohs.realworld.application.model.AuthenticationRequest;
import io.github.zoooohs.realworld.application.model.RegistrationRequest;
import io.github.zoooohs.realworld.application.model.UpdateUserRequest;
import io.github.zoooohs.realworld.application.model.UsersResponse;
import io.github.zoooohs.realworld.application.port.in.security.TokenReader;
import io.github.zoooohs.realworld.application.port.in.usecase.AuthenticationUseCase;
import io.github.zoooohs.realworld.domain.model.UserId;
import io.github.zoooohs.realworld.infrastructure.model.AuthenticationHttpRequest;
import io.github.zoooohs.realworld.infrastructure.model.RegistrationHttpRequest;
import io.github.zoooohs.realworld.infrastructure.model.SingleUserResponse;
import io.github.zoooohs.realworld.infrastructure.model.UpdateUserHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

//@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
    private final AuthenticationUseCase authenticationUseCase;
    private final TokenReader tokenReader;

    @PostMapping("/login")
    public SingleUserResponse authentication(@RequestBody AuthenticationHttpRequest request) {
        AuthenticationRequest authenticationRequest = request.user();
        UsersResponse userResponse = authenticationUseCase.authentication(authenticationRequest);
        return new SingleUserResponse(userResponse);
    }

    @PostMapping
    public SingleUserResponse registration(@RequestBody RegistrationHttpRequest request) {
        RegistrationRequest registrationRequest = request.user();
        UsersResponse userResponse = authenticationUseCase.registration(registrationRequest);
        return new SingleUserResponse(userResponse);
    }

    @GetMapping
    public SingleUserResponse getCurrentUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserId userId = parseUserIdFromToken(token);
        UsersResponse currentUser = authenticationUseCase.getCurrentUser(userId);
        return new SingleUserResponse(currentUser);
    }

    @PutMapping
    public SingleUserResponse updateUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody UpdateUserHttpRequest request
    ) {
        UserId userId = parseUserIdFromToken(token);
        UpdateUserRequest updateUserRequest = request.user();
        UsersResponse updatedUser = authenticationUseCase.updateCurrentUser(userId, updateUserRequest);
        return new SingleUserResponse(updatedUser);
    }

    private UserId parseUserIdFromToken(String token) {
        UserId userId = tokenReader.getUserId(token);
        if (userId == null) {
            // 401
        }
        return userId;
    }
}
