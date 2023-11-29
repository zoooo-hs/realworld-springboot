package io.github.zoooohs.realworld.infrastructure.model.persistance.user;

import io.github.zoooohs.realworld.domain.user.entity.User;
import io.github.zoooohs.realworld.domain.user.entity.UserId;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "users")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    private String id;
    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FollowEntity> followings;

    public static UserEntity of(String id) {
        return UserEntity.builder().id(id).build();
    }
    public static UserEntity fromDomainEntity(User user) {
        return UserEntity.builder()
                .id(user.getId().id())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .bio(user.getBio())
                .image(user.getImage())
                .followings(FollowEntity.userFollowings(user.getId(), user.getFollowings()))
                .build();
    }

    public User toDomainEntity() {
        return User.builder()
                .id(new UserId(id))
                .username(username)
                .email(email)
                .password(password)
                .bio(bio)
                .image(image)
                .followings(followings == null ? null :
                        followings.stream()
                                .map(FollowEntity::getFollowee)
                                .map(UserEntity::getId)
                                .map(UserId::new)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
