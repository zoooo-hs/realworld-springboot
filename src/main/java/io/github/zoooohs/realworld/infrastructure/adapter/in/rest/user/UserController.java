package io.github.zoooohs.realworld.infrastructure.adapter.in.rest.user;

import io.github.zoooohs.realworld.application.model.UpdateUserRequest;
import io.github.zoooohs.realworld.application.model.UsersResponse;
import io.github.zoooohs.realworld.application.port.in.usecase.AuthenticationUseCase;
import io.github.zoooohs.realworld.domain.model.UserId;
import io.github.zoooohs.realworld.infrastructure.model.rest.SingleUserResponse;
import io.github.zoooohs.realworld.infrastructure.model.rest.UpdateUserHttpRequest;
import io.github.zoooohs.realworld.infrastructure.model.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final AuthenticationUseCase authenticationUseCase;
    @GetMapping
    public SingleUserResponse getCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        UserId userId = UserPrincipal.userId(userPrincipal);
        UsersResponse currentUser = authenticationUseCase.getCurrentUser(userId);
        return new SingleUserResponse(currentUser);
    }

    @PutMapping
    public SingleUserResponse updateUser(
            @RequestBody UpdateUserHttpRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        UserId userId = UserPrincipal.userId(userPrincipal);
        UpdateUserRequest updateUserRequest = request.user();
        UsersResponse updatedUser = authenticationUseCase.updateCurrentUser(userId, updateUserRequest);
        return new SingleUserResponse(updatedUser);
    }
}
