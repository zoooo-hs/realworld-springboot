package io.github.zoooohs.realworld.application.service;

import io.github.zoooohs.realworld.application.exception.AlreadyFollowed;
import io.github.zoooohs.realworld.application.exception.NotFollowing;
import io.github.zoooohs.realworld.application.exception.UserNotFound;
import io.github.zoooohs.realworld.application.model.ProfileResponse;
import io.github.zoooohs.realworld.application.port.in.usecase.ProfileUseCase;
import io.github.zoooohs.realworld.application.port.out.persistance.user.UserRepository;
import io.github.zoooohs.realworld.domain.exception.AlreadyAdded;
import io.github.zoooohs.realworld.domain.exception.FollowingNotFound;
import io.github.zoooohs.realworld.domain.model.User;
import io.github.zoooohs.realworld.domain.model.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService implements ProfileUseCase {
    private final UserRepository userRepository;

    @Override
    public ProfileResponse getProfile(UserId currentUserId, String username) {
        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);

        boolean following;
        if (currentUserId == null) {
            following = false;
        } else {
            User currentUser = userRepository.getByUserId(currentUserId);
            following = currentUser.isFollowing(foundUser.getId());
        }

        return createProfileResponse(foundUser, following);
    }

    @Transactional
    @Override
    public ProfileResponse follow(UserId currentUserId, String username) {
        User followee = userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
        User currentUser = userRepository.getByUserId(currentUserId);

        try {
            currentUser.follow(followee.getId());
        } catch (AlreadyAdded e) {
            throw new AlreadyFollowed();
        }

        userRepository.save(currentUser);

        boolean following = currentUser.isFollowing(followee.getId());
        return createProfileResponse(followee, following);
    }

    @Transactional
    @Override
    public ProfileResponse unfollow(UserId currentUserId, String username) {
        User followee = userRepository.findByUsername(username)
                .orElseThrow(UserNotFound::new);
        User currentUser = userRepository.getByUserId(currentUserId);

        try {
            currentUser.unfollow(followee.getId());
        } catch (FollowingNotFound e) {
            throw new NotFollowing();
        }

        userRepository.save(currentUser);

        boolean following = currentUser.isFollowing(followee.getId());
        return createProfileResponse(followee, following);
    }

    private ProfileResponse createProfileResponse(User foundUser, boolean following) {
        return ProfileResponse
                .builder()
                .username(foundUser.getUsername())
                .bio(foundUser.getBio())
                .image(foundUser.getImage())
                .following(following)
                .build();
    }
}
