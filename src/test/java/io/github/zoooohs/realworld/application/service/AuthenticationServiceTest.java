package io.github.zoooohs.realworld.application.service;

import io.github.zoooohs.realworld.application.exception.DuplicatedEmail;
import io.github.zoooohs.realworld.application.exception.DuplicatedUsername;
import io.github.zoooohs.realworld.application.exception.NoMatchedAuthentication;
import io.github.zoooohs.realworld.application.model.AuthenticationRequest;
import io.github.zoooohs.realworld.application.model.RegistrationRequest;
import io.github.zoooohs.realworld.application.model.UpdateUserRequest;
import io.github.zoooohs.realworld.application.model.UsersResponse;
import io.github.zoooohs.realworld.application.port.in.usecase.AuthenticationUseCase;
import io.github.zoooohs.realworld.domain.User;
import io.github.zoooohs.realworld.domain.UserId;
import io.github.zoooohs.realworld.fake.FakePasswordManager;
import io.github.zoooohs.realworld.fake.FakeTokenWriter;
import io.github.zoooohs.realworld.fake.FakeUserIdGenerator;
import io.github.zoooohs.realworld.fake.FakeUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthenticationServiceTest {

    private FakeUserRepository userRepository;
    private FakeUserIdGenerator userIdGenerator;
    private FakePasswordManager passwordManager;
    private FakeTokenWriter tokenManager;
    private AuthenticationUseCase sut;

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        userIdGenerator = new FakeUserIdGenerator(-1L);
        passwordManager = new FakePasswordManager("fake");
        tokenManager = new FakeTokenWriter();

        sut = new AuthenticationService(userRepository, userIdGenerator, passwordManager, tokenManager);

        UserId userId = userIdGenerator.generate();
        String encryptedPassword = passwordManager.encrypt("123123");
        User duplicatedUser = new User(userId, "123@456.zz", encryptedPassword, "name");
        userRepository.save(duplicatedUser);
    }

    @Test
    void registrationRequestEmailIsNotEmailFormat() {

    }
    @Test
    void registrationRequestUserNameDuplicatedException() {
        // GIVEN
        RegistrationRequest request = new RegistrationRequest("abc@def.xyz", "123123","name");

        // WHEN, THEN
        Assertions.assertThrows(DuplicatedUsername.class, () -> sut.registration(request));
    }

    @Test
    void requestEmailDuplicatedException() {
        // GIVEN
        RegistrationRequest request = new RegistrationRequest("123@456.zz", "123123","name2");

        // WHEN, THEN
        Assertions.assertThrows(DuplicatedEmail.class, () -> sut.registration(request));
    }

    @Test
    void registrationSaveUserAndReturnUsersResponseWithToken() {
        // GIVEN
        RegistrationRequest request = new RegistrationRequest("abc@def.xyz", "123123","hyunsu");

        // WHEN
        UsersResponse result = sut.registration(request);

        // THEN
        Assertions.assertAll(
                () -> Assertions.assertEquals("abc@def.xyz", result.email()),
                () -> Assertions.assertEquals("hyunsu", result.username()),
                () -> Assertions.assertTrue(userRepository.existsByEmail("abc@def.xyz")),
                () -> Assertions.assertNotNull(result.token())
        );
    }

    @Test
    void registrationSavedUserPasswordNotTheSameAsRequest() {
        // GIVEN
        RegistrationRequest request = new RegistrationRequest("abc@def.xyz", "123123","hyunsu");

        // WHEN
        UsersResponse result = sut.registration(request);

        // THEN
        User savedUser = userRepository.getByUserName("hyunsu");
        Assertions.assertNotEquals(request.password(), savedUser.getPassword());
    }

    @Test
    void authenticationEmailNotFoundWrongAuthenticationInput() {
        // GIVEN
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("abc@def.zz", "123123");

        // WHEN, THEN
        Assertions.assertThrows(NoMatchedAuthentication.class, () -> sut.authentication(authenticationRequest));
    }

    @Test
    void authenticationPasswordNotMatchWrongAuthenticationInput() {
        // GIVEN
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("123@456.zz", "wrongpassword");

        // WHEN, THEN
        Assertions.assertThrows(NoMatchedAuthentication.class, () -> sut.authentication(authenticationRequest));
    }

    @Test
    void authenticationMatchEmailAndPasswordReturnUsersResponseWithToken() {
        // GIVEN
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("123@456.zz", "123123");

        // WHEN
        UsersResponse result = sut.authentication(authenticationRequest);

        // THEN
        Assertions.assertAll(
                () -> Assertions.assertEquals("123@456.zz", result.email()),
                () -> Assertions.assertEquals("name", result.username()),
                () -> Assertions.assertNotNull(result.token())
        );
    }
    @Test
    void getCurrentUserReturnCurrentUserIdUsersResponse() {
        // GIVEN
        UserId currentUserId = new UserId(0L);

        // WHEN
        UsersResponse currentUser = sut.getCurrentUser(currentUserId);

        // THEN
        Assertions.assertAll(
                () -> Assertions.assertEquals("123@456.zz", currentUser.email()),
                () -> Assertions.assertEquals("name", currentUser.username()),
                () -> Assertions.assertNotNull(currentUser.token())
        );
    }

    @Test
    void updateUserEmailDuplication() {
        // GIVEN
        UserId currentUserId = new UserId(0L);
        UpdateUserRequest emailDuplicatedUpdateRequest =
                UpdateUserRequest.builder()
                        .email("123@456.zz")
                        .build();

        // WHEN, THEN
        Assertions.assertThrows(DuplicatedEmail.class, () -> sut.updateCurrentUser(currentUserId, emailDuplicatedUpdateRequest));
    }

    @Test
    void updateUserUsernameDuplication() {
        // GIVEN
        UserId currentUserId = new UserId(0L);
        UpdateUserRequest emailDuplicatedUpdateRequest =
                UpdateUserRequest.builder()
                        .username("name")
                        .build();

        // WHEN, THEN
        Assertions.assertThrows(DuplicatedUsername.class, () -> sut.updateCurrentUser(currentUserId, emailDuplicatedUpdateRequest));
    }

    @Test
    void updateReturnUsersResponseWithUpdatedValueAndUpdatedDB() {
        UserId currentUserId = new UserId(0L);
        UpdateUserRequest emailDuplicatedUpdateRequest =
                UpdateUserRequest.builder()
                        .email("new@email.com")
                        .username("newname")
                        .bio("new bio")
                        .image("http://new-image/a.jpg")
                        .build();

        // WHEN
        UsersResponse updatedUser = sut.updateCurrentUser(currentUserId, emailDuplicatedUpdateRequest);

        // THEN
        Assertions.assertAll(
                () -> Assertions.assertEquals("new@email.com", updatedUser.email()),
                () -> Assertions.assertEquals("newname", updatedUser.username()),
                () -> Assertions.assertEquals("new bio", updatedUser.bio()),
                () -> Assertions.assertEquals("http://new-image/a.jpg", updatedUser.image()),
                () -> Assertions.assertNotNull(updatedUser.token())
        );
    }
}