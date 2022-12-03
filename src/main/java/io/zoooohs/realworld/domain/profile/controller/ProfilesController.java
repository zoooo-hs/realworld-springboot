package io.zoooohs.realworld.domain.profile.controller;

import io.zoooohs.realworld.domain.profile.dto.ProfileDto;
import io.zoooohs.realworld.domain.profile.service.ProfileService;
import io.zoooohs.realworld.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfilesController {
    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ProfileDto.Single getProfile(@PathVariable("username") String name, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        return new ProfileDto.Single(profileService.getProfile(name, authUserDetails));
    }

    @PostMapping("/{username}/follow")
    public ProfileDto.Single followUser(@PathVariable("username") String name, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        return new ProfileDto.Single(profileService.followUser(name, authUserDetails));
    }

    @DeleteMapping("/{username}/follow")
    public ProfileDto.Single unfollowUser(@PathVariable("username") String name, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        return new ProfileDto.Single(profileService.unfollowUser(name, authUserDetails));
    }
}
