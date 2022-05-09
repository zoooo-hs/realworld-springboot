package io.zoooohs.realworld.domain.profile.service;

import io.zoooohs.realworld.domain.profile.dto.ProfileDto;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceImplTest {
    ProfileService profileService;

    UserDto.Auth authUser;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        profileService = new ProfileServiceImpl(userRepository);
        authUser = UserDto.Auth.builder()
                .id(1L)
                .email("email@email.com")
                .name("testUser")
                .bio("bio")
                .image("photo-path")
                .build();
    }

    @Test
    void whenValidUsername_thenReturnProfile() {
        UserEntity expectedUser = UserEntity.builder()
                .id(2L)
                .name("expectedUser")
                .email("expected@email.com")
                .bio("expected bio")
                .image("expected_image_path")
                .build();

        when(userRepository.findByName(eq(expectedUser.getName()))).thenReturn(Optional.of(expectedUser));

        ProfileDto actual = profileService.getProfile(expectedUser.getName(), authUser);

        assertEquals(expectedUser.getName(), actual.getName());
        assertEquals(expectedUser.getBio(), actual.getBio());
        assertEquals(expectedUser.getImage(), actual.getImage());
    }
}
