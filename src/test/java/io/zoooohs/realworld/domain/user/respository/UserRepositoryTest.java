package io.zoooohs.realworld.domain.user.respository;

import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void whenSave_thenCanBeFound() {
        UserEntity user = UserEntity.builder().username("username").email("test@test.com").password("password").bio("").build();
        Long id = userRepository.save(user).getId();

        Optional<UserEntity> maybeUserEntity = userRepository.findById(id);

        assertTrue(maybeUserEntity.isPresent());
    }

    @MethodSource("validUserRegistration")
    @ParameterizedTest
    void whenUsernameOrEmailExist_thenUserEntityFound(String username, String email) {
        UserEntity user = UserEntity.builder().username("username").email("test@test.com").password("password").bio("").build();
        userRepository.save(user);

        List<UserEntity> actual = userRepository.findByUsernameOrEmail(username, email);

        assertTrue(actual.size() > 0);
    }

    public static Stream<Arguments> validUserRegistration() {
        return Stream.of(
                Arguments.of(null, "test@test.com"),
                Arguments.of("username", null),
                Arguments.of("username", "test@test.com")
        );
    }
}