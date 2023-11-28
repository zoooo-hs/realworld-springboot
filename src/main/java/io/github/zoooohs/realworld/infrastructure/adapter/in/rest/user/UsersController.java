package io.github.zoooohs.realworld.infrastructure.adapter.in.rest.user;

import io.github.zoooohs.realworld.application.model.user.AuthenticationRequest;
import io.github.zoooohs.realworld.application.model.user.RegistrationRequest;
import io.github.zoooohs.realworld.application.model.user.UsersResponse;
import io.github.zoooohs.realworld.application.port.in.usecase.user.AuthenticationUseCase;
import io.github.zoooohs.realworld.infrastructure.model.rest.AuthenticationHttpRequest;
import io.github.zoooohs.realworld.infrastructure.model.rest.RegistrationHttpRequest;
import io.github.zoooohs.realworld.infrastructure.model.rest.SingleUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
    private final AuthenticationUseCase authenticationUseCase;

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
}
