package io.zoooohs.realworld.domain.profile.service;

import io.zoooohs.realworld.domain.profile.dto.ProfileDto;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.repository.UserRepository;
import io.zoooohs.realworld.exception.AppException;
import io.zoooohs.realworld.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;

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
}
