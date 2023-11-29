package io.github.zoooohs.realworld.application.service;

import io.github.zoooohs.realworld.application.exception.AlreadyFollowed;
import io.github.zoooohs.realworld.application.exception.NotFollowing;
import io.github.zoooohs.realworld.application.exception.UserNotFound;
import io.github.zoooohs.realworld.application.model.user.ProfileResponse;
import io.github.zoooohs.realworld.domain.user.entity.User;
import io.github.zoooohs.realworld.domain.user.entity.UserId;
import io.github.zoooohs.realworld.fake.FakePasswordManager;
import io.github.zoooohs.realworld.fake.FakeUserIdGenerator;
import io.github.zoooohs.realworld.fake.FakeUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfileServiceTest {
    private ProfileService sut;
    private FakeUserRepository userRepository = new FakeUserRepository();
    private FakeUserIdGenerator userIdGenerator;
    private FakePasswordManager passwordManager;
    private UserId userId;

    @BeforeEach
    void setUp() {
        sut = new ProfileService(userRepository);
        userIdGenerator = new FakeUserIdGenerator(-1L);
        passwordManager = new FakePasswordManager("fake");

        userId = userIdGenerator.generate();
        User currentUser = User.builder()
                .id(userId)
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(currentUser);
    }

    @Test
    void getProfileUsernameNotFound() {
        // GIVEN
        UserId currentUserId = userId;
        // GIVEN, WHEN, THEN
        Assertions.assertThrows(UserNotFound.class, () -> sut.getProfile(currentUserId, "username"));
    }

    @Test
    void getProfileCurrentUserIdNullReturnFollowingFalse() {
        // GIVEN
        User savedUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.zz")
                .username("name")
                .bio("some bio")
                .image("http://image-a/a.jpg")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(savedUser);

        // WHEN
        ProfileResponse profile = sut.getProfile(null, "name");

        // THEN
        Assertions.assertAll(
                () -> assertEquals("name", profile.username()),
                () -> assertEquals("some bio", profile.bio()),
                () -> assertEquals("http://image-a/a.jpg", profile.image()),
                () -> assertFalse(profile.following())
        );
    }

    @Test
    void getProfileReturnsProfileResponseWithFollowingField() {
        // GIVEN
        UserId currentUserId = userId;
        User savedUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.zz")
                .username("name")
                .bio("some bio")
                .image("http://image-a/a.jpg")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(savedUser);

        // WHEN
        ProfileResponse profile = sut.getProfile(currentUserId, "name");

        // THEN
        Assertions.assertAll(
                () -> assertEquals("name", profile.username()),
                () -> assertEquals("some bio", profile.bio()),
                () -> assertEquals("http://image-a/a.jpg", profile.image()),
                () -> assertFalse(profile.following())
        );
    }

    @Test
    void getProfileItSelfReturnProfileWithFollowingFalse() {
        // GIVEN
        UserId currentUserId = userId;
        User savedUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.zz")
                .username("name")
                .bio("some bio")
                .image("http://image-a/a.jpg")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(savedUser);


        // WHEN
        ProfileResponse profile = sut.getProfile(currentUserId, "name");

        // THEN
        Assertions.assertAll(
                () -> assertEquals("name", profile.username()),
                () -> assertEquals("some bio", profile.bio()),
                () -> assertEquals("http://image-a/a.jpg", profile.image()),
                () -> assertFalse(profile.following())
        );
    }

    @Test
    void getProfileIfUserFollowerContainsCurrentUserFollowingTrue() {
        // GIVEN
        UserId followeeId = userIdGenerator.generate();
        UserId followerId = userIdGenerator.generate();
        User followee = User.builder()
                .id(followeeId)
                .email("123@456.zz")
                .username("followee")
                .bio("some bio")
                .image("http://image-a/a.jpg")
                .password(passwordManager.encrypt("123123"))
                .followers(List.of(followerId))
                .build();
        userRepository.save(followee);

        User follower = User.builder()
                .id(followerId)
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(follower);

        // WHEN
        ProfileResponse profile = sut.getProfile(followerId, "followee");

        // THEN
        Assertions.assertAll(
                () -> assertEquals("followee", profile.username()),
                () -> assertEquals("some bio", profile.bio()),
                () -> assertEquals("http://image-a/a.jpg", profile.image()),
                () -> assertTrue(profile.following())
        );
    }

    @Test
    void followUsernameNotFound() {
        // GIVEN
        UserId currentUserId = userId;

        // WHEN, THEN
        Assertions.assertThrows(UserNotFound.class, () -> sut.follow(currentUserId, "followee"));
    }

    @Test
    void followAlreadyFollowedThrowsAlreadyFollowed() {
        // GIVEN
        UserId followerId = userIdGenerator.generate();
        User followee = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.zz")
                .username("followee")
                .bio("some bio")
                .image("http://image-a/a.jpg")
                .password(passwordManager.encrypt("123123"))
                .followers(List.of(followerId))
                .build();
        userRepository.save(followee);

        User follower = User.builder()
                .id(followerId)
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(follower);


        // WHEN, THEN
        Assertions.assertThrows(AlreadyFollowed.class, () -> sut.follow(followerId, "followee"));
    }

    @Test
    void followReturnProfileWithFollowingTrue() {
        // GIVEN
        User followee = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.zz")
                .username("followee")
                .bio("some bio")
                .image("http://image-a/a.jpg")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(followee);

        UserId followerId = userId;

        // WHEN
        ProfileResponse profile = sut.follow(userId, "followee");

        // THEN
        Assertions.assertAll(
                () -> assertEquals("followee", profile.username()),
                () -> assertEquals("some bio", profile.bio()),
                () -> assertEquals("http://image-a/a.jpg", profile.image()),
                () -> assertTrue(profile.following()),
                () -> assertTrue(sut.getProfile(followerId, "followee").following())
        );
    }

    @Test
    void unfollowUsernameUserNotFound() {
        // GIVEN
        UserId followerId = userId;

        // WHEN, THEN
        Assertions.assertThrows(UserNotFound.class, () -> sut.unfollow(followerId, "followee"));
    }

    @Test
    void unfollowNotFollowing() {
        // GIVEN
        User followee = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.zz")
                .username("followee")
                .bio("some bio")
                .image("http://image-a/a.jpg")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(followee);

        UserId followerId = userId;

        // WHEN, THEN
        Assertions.assertThrows(NotFollowing.class, () -> sut.unfollow(followerId, "followee"));
    }

    @Test
    void unfollowReturnProfilesWithFollowingFalse() {
        // GIVEN
        UserId followerId = userIdGenerator.generate();
        List<UserId> followers = new ArrayList<>();
        followers.add(followerId);
        User followee = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.zz")
                .username("followee")
                .bio("some bio")
                .image("http://image-a/a.jpg")
                .password(passwordManager.encrypt("123123"))
                .followers(followers)
                .build();
        userRepository.save(followee);

        User follower = User.builder()
                .id(followerId)
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(follower);

        // WHEN
        ProfileResponse profile = sut.unfollow(followerId, "followee");

        // THEN
        Assertions.assertAll(
                () -> assertEquals("followee", profile.username()),
                () -> assertEquals("some bio", profile.bio()),
                () -> assertEquals("http://image-a/a.jpg", profile.image()),
                () -> assertFalse(profile.following()),
                () -> assertFalse(sut.getProfile(followerId, "followee").following())
        );
    }
}