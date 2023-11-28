package io.github.zoooohs.realworld.infrastructure.adapter.in.rest.user;

import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import io.github.zoooohs.realworld.application.port.in.usecase.user.ProfileFollowUseCase;
import io.github.zoooohs.realworld.application.port.in.usecase.user.ProfileUseCase;
import io.github.zoooohs.realworld.domain.model.user.UserId;
import io.github.zoooohs.realworld.infrastructure.model.rest.SingleProfileResponse;
import io.github.zoooohs.realworld.infrastructure.model.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfilesController {
    private final ProfileUseCase profileUseCase;
    private final ProfileFollowUseCase profileFollowUseCase;

    @GetMapping("/{username}")
    public SingleProfileResponse getProfile(
            @PathVariable("username") String username,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        UserId userId = UserPrincipal.userId(userPrincipal);
        ProfileResponse profile = profileUseCase.getProfile(userId, username);
        return new SingleProfileResponse(profile);
    }

    @PostMapping("/{username}/follow")
    public SingleProfileResponse follow(
            @PathVariable("username") String username,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        UserId userId = UserPrincipal.userId(userPrincipal);
        ProfileResponse profile = profileFollowUseCase.follow(userId, username);
        return new SingleProfileResponse(profile);
    }

    @DeleteMapping("/{username}/follow")
    public SingleProfileResponse unfollow(
            @PathVariable("username") String username,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        UserId userId = UserPrincipal.userId(userPrincipal);
        ProfileResponse profile = profileFollowUseCase.unfollow(userId, username);
        return new SingleProfileResponse(profile);
    }

}
