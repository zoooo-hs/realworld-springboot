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
        // TODO: add following information
        return userRepository.findByName(name).map(entity -> ProfileDto.builder()
                .name(name)
                .bio(entity.getBio())
                .image(entity.getImage())
                .following(false)
                .build())
                .orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
    }

    @Transactional
    @Override
    public ProfileDto followUser(String name, UserDto.Auth authUser) {
        UserEntity followee = userRepository.findByName(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        UserEntity follower = UserEntity.builder().id(authUser.getId()).build(); // myself

        FollowEntity follow =  FollowEntity.builder().followee(followee).follower(follower).build();
        followRepository.save(follow);

        return ProfileDto.builder()
                .name(followee.getName())
                .bio(followee.getBio())
                .image(followee.getImage())
                .following(true)
                .build();
    }
}
