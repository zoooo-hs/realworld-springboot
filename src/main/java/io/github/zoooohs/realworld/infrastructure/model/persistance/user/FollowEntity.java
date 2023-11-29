package io.github.zoooohs.realworld.infrastructure.model.persistance.user;

import io.github.zoooohs.realworld.domain.user.entity.UserId;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "user_follow_relations")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowEntity {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "followee_id")
    private UserEntity followee;

    public static Set<FollowEntity> userFollowers(UserId followeeId, List<UserId> followers) {
        if (followers == null) {
            return Collections.emptySet();
        }
        UserEntity followee = UserEntity.of(followeeId.id());
        return followers.stream()
                .map(UserId::id)
                .map(UserEntity::of)
                .map(follower -> FollowEntity.builder().followee(followee).follower(follower).build())
                .collect(Collectors.toSet());
    }
}
