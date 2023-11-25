package io.github.zoooohs.realworld.application.service;

import io.github.zoooohs.realworld.application.exception.AlreadyFollowed;
import io.github.zoooohs.realworld.application.exception.NotFollowing;
import io.github.zoooohs.realworld.application.exception.UserNotFound;
import io.github.zoooohs.realworld.application.model.ProfileResponse;
import io.github.zoooohs.realworld.application.port.in.usecase.ProfileUseCase;
import io.github.zoooohs.realworld.domain.model.User;
import io.github.zoooohs.realworld.domain.model.UserId;
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
    private ProfileUseCase sut;
    private FakeUserRepository userRepository = new FakeUserRepository();
    private FakeUserIdGenerator userIdGenerator;
    private FakePasswordManager passwordManager;

    @BeforeEach
    void setUp() {
        sut = new ProfileService(userRepository);
        userIdGenerator = new FakeUserIdGenerator(-1L);
        passwordManager = new FakePasswordManager("fake");
    }

    @Test
    void getProfileUsernameNotFound() {
        // GIVEN, WHEN, THEN
        Assertions.assertThrows(UserNotFound.class, () -> sut.getProfile(new UserId(1L), "username"));
    }

    @Test
    void getProfileReturnsProfileResponseWithFollowingField() {
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
        User currentUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(currentUser);

        // WHEN
        ProfileResponse profile = sut.getProfile(new UserId(1L), "name");

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
        ProfileResponse profile = sut.getProfile(new UserId(0L), "name");

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
        User followee = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.zz")
                .username("followee")
                .bio("some bio")
                .image("http://image-a/a.jpg")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(followee);

        User currentUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .followings(List.of(new UserId(0L)))
                .build();
        userRepository.save(currentUser);

        // WHEN
        ProfileResponse profile = sut.getProfile(new UserId(1L), "followee");

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
        User currentUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(currentUser);

        // WHEN, THEN
        Assertions.assertThrows(UserNotFound.class, () -> sut.follow(new UserId(0L), "followee"));
    }

    @Test
    void followAlreadyFollowedThrowsAlreadyFollowed() {
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

        User currentUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .followings(List.of(followee.getId()))
                .build();
        userRepository.save(currentUser);


        // WHEN, THEN
        Assertions.assertThrows(AlreadyFollowed.class, () -> sut.follow(new UserId(1L), "followee"));
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

        User currentUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(currentUser);

        // WHEN
        ProfileResponse profile = sut.follow(new UserId(1L), "followee");

        // THEN
        Assertions.assertAll(
                () -> assertEquals("followee", profile.username()),
                () -> assertEquals("some bio", profile.bio()),
                () -> assertEquals("http://image-a/a.jpg", profile.image()),
                () -> assertTrue(profile.following()),
                () -> assertTrue(sut.getProfile(new UserId(1L), "followee").following())
        );
    }

    @Test
    void unfollowUsernameUserNotFound() {
        // GIVEN
        User currentUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(currentUser);

        // WHEN, THEN
        Assertions.assertThrows(UserNotFound.class, () -> sut.unfollow(new UserId(0L), "followee"));
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

        User currentUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .build();
        userRepository.save(currentUser);

        // WHEN, THEN
        Assertions.assertThrows(NotFollowing.class, () -> sut.unfollow(new UserId(1L), "followee"));
    }

    @Test
    void unfollowReturnProfilesWithFollowingFalse() {
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

        List<UserId> following = new ArrayList<>();
        following.add(followee.getId());
        User currentUser = User.builder()
                .id(userIdGenerator.generate())
                .email("123@456.yy")
                .username("currentUser")
                .password(passwordManager.encrypt("123123"))
                .followings(following)
                .build();
        userRepository.save(currentUser);

        // WHEN
        ProfileResponse profile = sut.unfollow(new UserId(1L), "followee");

        // THEN
        Assertions.assertAll(
                () -> assertEquals("followee", profile.username()),
                () -> assertEquals("some bio", profile.bio()),
                () -> assertEquals("http://image-a/a.jpg", profile.image()),
                () -> assertFalse(profile.following()),
                () -> assertFalse(sut.getProfile(new UserId(1L), "followee").following())
        );
    }
}