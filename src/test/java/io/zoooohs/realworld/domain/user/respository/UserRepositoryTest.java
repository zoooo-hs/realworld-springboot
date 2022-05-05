package io.zoooohs.realworld.domain.user.respository;

import io.zoooohs.realworld.domain.user.entity.UserEntity;
import io.zoooohs.realworld.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private UserEntity savedUser;

    @BeforeEach
    void setUp() {
        savedUser = UserEntity.builder().name("username").email("test@test.com").password("password").bio("").build();
        userRepository.save(savedUser);
    }


    @Test
    void whenSave_thenCanBeFound() {
        Long id = savedUser.getId();

        Optional<UserEntity> maybeUserEntity = userRepository.findById(id);

        assertTrue(maybeUserEntity.isPresent());
    }

    @MethodSource("validUserRegistration")
    @ParameterizedTest
    void whenUsernameOrEmailExist_thenUserEntityFound(String username, String email) {
        List<UserEntity> actual = userRepository.findByNameOrEmail(username, email);

        assertTrue(actual.size() > 0);
    }

    public static Stream<Arguments> validUserRegistration() {
        return Stream.of(
                Arguments.of(null, "test@test.com"),
                Arguments.of("username", null),
                Arguments.of("username", "test@test.com")
        );
    }

    @Test
    void whenEmailExist_thenUserEntityFound() {
        String email = savedUser.getEmail();

        Optional<UserEntity> maybeUser = userRepository.findByEmail(email);

        assertTrue(maybeUser.isPresent());
    }

    @Test
    void whenNameExist_thenUserEntityFound() {
        String name = savedUser.getName();

        Optional<UserEntity> maybeUser = userRepository.findByName(name);

        assertTrue(maybeUser.isPresent());
    }
}