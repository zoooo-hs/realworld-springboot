package io.zoooohs.realworld.domain.user.service;

import io.zoooohs.realworld.exception.Error;
import io.zoooohs.realworld.exception.AppException;
import io.zoooohs.realworld.security.JwtUtils;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registration(final UserDto.Registration registration) {
        userRepository.findByUsernameOrEmail(registration.getUsername(), registration.getEmail()).stream().findAny().ifPresent(entity -> {throw new AppException(Error.DUPLICATED_USER);});
        UserEntity userEntity = UserEntity.builder().username(registration.getUsername()).email(registration.getEmail()).password(passwordEncoder.encode(registration.getPassword())).bio("").build();
        userRepository.save(userEntity);
        return convertEntityToDto(userEntity);
    }

    private UserDto convertEntityToDto(UserEntity userEntity) {
        return UserDto.builder().username(userEntity.getUsername()).bio(userEntity.getBio()).email(userEntity.getEmail()).image(userEntity.getImage()).token(jwtUtils.encode(userEntity.getUsername())).build();
    }
}
