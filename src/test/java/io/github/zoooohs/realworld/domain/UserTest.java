package io.github.zoooohs.realworld.domain;

import io.github.zoooohs.realworld.application.port.out.persistance.user.UserIdGenerator;
import io.github.zoooohs.realworld.domain.exception.AlreadyAdded;
import io.github.zoooohs.realworld.domain.exception.FollowingNotFound;
import io.github.zoooohs.realworld.domain.model.User;
import io.github.zoooohs.realworld.domain.model.UserId;
import io.github.zoooohs.realworld.fake.FakeUserIdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class UserTest {

    private UserIdGenerator userIdGenerator;

    @BeforeEach
    void setUp() {
        userIdGenerator = new FakeUserIdGenerator(-1L);
    }

    @Test
    void newUserGetNonBlankEmailUsernamePassword() {
        UserId userId = newId();
        Assertions.assertThrows(AssertionError.class, () -> { new User(userId, "", "", "");});
    }

    private UserId newId() {
        return userIdGenerator.generate();
    }

    @Test
    void newUserEmailMustBeEmailFormat() {
        UserId userId = newId();
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> { new User(userId,"notemailform", "hyunsu", "123123");}
        );
    }

    @Test
    void newUser() {
        UserId userId = newId();
        new User(userId, "abc@def.xyz", "hyunsu", "123123") ;
    }


    @Test
    void isFollowingWithInputNullReturnFalse() {
        // GIVEN
        User user = User.builder()
                .id(newId())
                .email("123@456.zz")
                .username("name")
                .password("123123")
                .build();

        // WHEN
        boolean following = user.isFollowing(null);

        // THEN
        Assertions.assertFalse(following);
    }

    @Test
    void isFollowingInputContainsInFollowerReturnTrue() {
        UserId followerId = newId();
        UserId followeeId = newId();
        User user = User.builder()
                .id(followerId)
                .email("123@456.zz")
                .username("followee")
                .password("123123")
                .followings(List.of(followeeId))
                .build();

        // WHEN
        boolean following = user.isFollowing(followeeId);

        // THEN
        Assertions.assertTrue(following);
    }

    @Test
    void isFollowingInputNotContainsInFollowerReturnFalse() {
        User user = User.builder()
                .id(newId())
                .email("123@456.zz")
                .username("followee")
                .password("123123")
                .followings(List.of(newId()))
                .build();

        UserId notFollowingUserId = newId();

        // WHEN
        boolean following = user.isFollowing(notFollowingUserId);

        // THEN
        Assertions.assertFalse(following);
    }

    @Test
    void isFollowingFollowerNullReturnFalse() {
        User user = User.builder()
                .id(newId())
                .email("123@456.zz")
                .username("followee")
                .password("123123")
                .followings(null)
                .build();

        UserId anyUserId = newId();

        // WHEN
        boolean following = user.isFollowing(anyUserId);

        // THEN
        Assertions.assertFalse(following);
    }

    @Test
    void isFollowingFollowerEmptyReturnFalse() {
        User user = User.builder()
                .id(newId())
                .email("123@456.zz")
                .username("followee")
                .password("123123")
                .followings(Collections.emptyList())
                .build();

        UserId anyUserId = newId();

        // WHEN
        boolean following = user.isFollowing(anyUserId);

        // THEN
        Assertions.assertFalse(following);
    }

    @Test
    void followAlreadyAddedThrows() {
        // GIVEN
        UserId alreadyFollowedId = newId();
        User user = User.builder()
                .id(newId())
                .email("123@456.zz")
                .username("followee")
                .password("123123")
                .followings(List.of(alreadyFollowedId))
                .build();

        // WHEN, THEN
        Assertions.assertThrows(AlreadyAdded.class, () -> user.follow(alreadyFollowedId));
    }

    @Test
    void followMakeIsFollowedByTrue() throws Exception {
        // GIVEN
        User user = User.builder()
                .id(newId())
                .email("123@456.zz")
                .username("followee")
                .password("123123")
                .followings(new ArrayList<>())
                .build();

        UserId followeeId = newId();
        user.follow(followeeId);

        // WHEN,
        boolean following = user.isFollowing(followeeId);

        // THEN
        Assertions.assertTrue(following);
    }

    @Test
    void followIfFollowingNullCreateEmptyListAndAdd() throws Exception {
        // GIVEN
        User user = User.builder()
                .id(newId())
                .email("123@456.zz")
                .username("followee")
                .password("123123")
                .followings(null)
                .build();

        UserId followeeId = newId();
        user.follow(followeeId);

        // WHEN,
        boolean following = user.isFollowing(followeeId);

        // THEN
        Assertions.assertTrue(following);
    }

    @Test
    void unfollowFollowingNotFound() {
        // GIVEN
        User user = User.builder()
                .id(newId())
                .email("123@456.zz")
                .username("followee")
                .password("123123")
                .build();

        UserId notFollowingUserId = newId();

        // WHEN, THEN
        Assertions.assertThrows(FollowingNotFound.class, () -> user.unfollow(notFollowingUserId));
    }

    @Test
    void unfollowMakeIsFollowingFalse() throws Exception {
        // GIVEN
        UserId followeeId = newId();
        List<UserId> followings = new ArrayList<>();
        followings.add(followeeId);
        User user = User.builder()
                .id(newId())
                .email("123@456.zz")
                .username("followee")
                .password("123123")
                .followings(followings)
                .build();

        // WHEN
        user.unfollow(followeeId);

        // THEN
        Assertions.assertFalse(user.isFollowing(followeeId));
    }
}