package io.github.zoooohs.realworld.domain.model.user;

import io.github.zoooohs.realworld.domain.exception.AlreadyAdded;
import io.github.zoooohs.realworld.domain.exception.FollowingNotFound;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private final UserId id;
    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;
    private List<UserId> followings;

    public User(UserId id, String email, String password, String username) {
        this.id = id;
        setUsername(username);
        setEmail(email);
        setPassword(password);
    }

    private void setEmail(String email) {
        assert email != null && !email.isEmpty();

        // TODO: email validation의 역할은 user의 일인가?
        if (invalidEmailForm(email)) {
            throw new IllegalArgumentException("invalid email form");
        }

        this.email = email;
    }

    private boolean invalidEmailForm(String email) {
        return !Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
                .matcher(email)
                .matches();
    }

    private void setUsername(String username) {
        assert username != null && !username.isEmpty();
        this.username = username;
    }

    private void setPassword(String password) {
        assert password != null && !password.isEmpty();
        this.password = password;
    }

    public void changeEmail(String email) {
        setEmail(email);
    }

    public void changeUsername(String username) {
        setUsername(username);
    }

    public void changePassword(String password) {
        setPassword(password);
    }

    public void changeBio(String bio) {
        setBio(bio);
    }

    private void setBio(String bio) {
        this.bio = bio;
    }

    public void changeImage(String image) {
        setImage(image);
    }

    private void setImage(String image) {
        this.image = image;
    }

    public boolean isFollowing(UserId userId) {
        if (userId == null) {
            return false;
        }
        if (followings == null) return false;
        return followings.contains(userId);
    }

    public void follow(UserId userId) throws AlreadyAdded {
        if (isFollowing(userId)) {
            throw new AlreadyAdded();
        }
        if (followings == null) {
            followings = new ArrayList<>();
        }
        followings.add(userId);
    }

    public void unfollow(UserId userId) throws FollowingNotFound {
        if (!isFollowing(userId)) {
            throw new FollowingNotFound();
        }
        followings.remove(userId);
    }
}
