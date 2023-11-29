package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import io.github.zoooohs.realworld.application.port.out.persistance.query.ProfileQueryHandler;
import io.github.zoooohs.realworld.domain.user.entity.User;
import io.github.zoooohs.realworld.domain.user.entity.UserId;
import io.github.zoooohs.realworld.domain.user.service.UserRepository;

import java.util.Optional;

public class FakeProfileQueryHandler implements ProfileQueryHandler {
    private final UserRepository userRepository;

    public FakeProfileQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ProfileResponse> findProfile(UserId currentUserId, String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) return Optional.empty();
        boolean followedBy = userOptional.get().isFollowedBy(currentUserId);
        return userOptional.map(foundUser -> ProfileResponse
                .builder()
                .username(foundUser.getUsername())
                .bio(foundUser.getBio())
                .image(foundUser.getImage())
                .following(followedBy)
                .build());
    }
}
