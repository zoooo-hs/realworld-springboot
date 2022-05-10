package io.zoooohs.realworld.domain.profile.entity;

import io.zoooohs.realworld.domain.common.entity.BaseEntity;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follows", uniqueConstraints = {
        @UniqueConstraint(name = "u_follow_followee_pair_must_be_unique", columnNames = {"followee", "follower"})
})
public class FollowEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "followee")
    private UserEntity followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "follower")
    private UserEntity follower;
}
