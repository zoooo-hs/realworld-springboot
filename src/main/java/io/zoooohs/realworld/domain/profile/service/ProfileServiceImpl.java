package io.zoooohs.realworld.domain.profile.service;

import io.zoooohs.realworld.domain.profile.dto.ProfileDto;
import io.zoooohs.realworld.domain.profile.entity.FollowEntity;
import io.zoooohs.realworld.domain.profile.repository.FollowRepository;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.domain.user.repository.UserRepository;
import io.zoooohs.realworld.exception.AppException;
import io.zoooohs.realworld.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    public ProfileDto getProfile(String name, UserDto.Auth authUser) {
        UserEntity user = userRepository.findByName(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        Boolean following = followRepository.findByFolloweeIdAndFollowerId(user.getId(), authUser.getId()).isPresent();

        return convertToProfile(user, following);
    }

    @Transactional
    @Override
    public ProfileDto followUser(String name, UserDto.Auth authUser) {
        UserEntity followee = userRepository.findByName(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        UserEntity follower = UserEntity.builder().id(authUser.getId()).build(); // myself

        followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .ifPresent(follow -> {throw new AppException(Error.ALREADY_FOLLOWED_USER);});

        FollowEntity follow =  FollowEntity.builder().followee(followee).follower(follower).build();
        followRepository.save(follow);

        return convertToProfile(followee, true);
    }

    @Transactional
    @Override
    public ProfileDto unfollowUser(String name, UserDto.Auth authUser) {
        UserEntity followee = userRepository.findByName(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        UserEntity follower = UserEntity.builder().id(authUser.getId()).build(); // myself

        FollowEntity follow = followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .orElseThrow(() -> new AppException(Error.FOLLOW_NOT_FOUND));
        followRepository.delete(follow);

        return convertToProfile(followee, false);
    }

    private ProfileDto convertToProfile(UserEntity user, Boolean following) {
        return ProfileDto.builder()
                .name(user.getName())
                .bio(user.getBio())
                .image(user.getImage())
                .following(following)
                .build();
    }
}
