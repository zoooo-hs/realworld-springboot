package io.zoooohs.realworld.domain.user.service;

import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.domain.user.repository.UserRepository;
import io.zoooohs.realworld.exception.Error;
import io.zoooohs.realworld.exception.AppException;
import io.zoooohs.realworld.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class UserServiceImplTest {
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtUtils jwtUtils;
    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, jwtUtils, passwordEncoder);
    }

    @Test
    void whenValidRegistrationInfo_thenSaveNewUserAndReturnNewUserDto() {
        UserDto.Registration registration = UserDto.Registration.builder().email("test@test.com").username("testman").password("password").build();

        when(jwtUtils.encode(anyString())).thenReturn("token.test.needed");
        when(passwordEncoder.encode(anyString())).thenReturn("b{testpasswordencodedstring}");

        UserDto actual = userService.registration(registration);

        verify(userRepository, times(1)).save(any(UserEntity.class));

        assertEquals(registration.getEmail(), actual.getEmail());
        assertEquals(registration.getUsername(), actual.getUsername());
        assertEquals("", actual.getBio());
        assertNull(actual.getImage());
        assertNotNull(actual.getToken());
    }

    @Test
    void whenDuplicatedUserRegistration_thenThrowDuplicationException() {
        UserDto.Registration registration = UserDto.Registration.builder().email("test@test.com").username("testman").password("password").build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(List.of(UserEntity.builder().build()));

        try {
            userService.registration(registration);
            fail();
        } catch (AppException e) {
            assertEquals(Error.DUPLICATED_USER, e.getError());
        } catch (Exception e) {
            fail();
        }
    }
}
