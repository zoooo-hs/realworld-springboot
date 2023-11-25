package io.github.zoooohs.realworld.application.service;

import io.github.zoooohs.realworld.application.exception.DuplicatedEmail;
import io.github.zoooohs.realworld.application.exception.DuplicatedUsername;
import io.github.zoooohs.realworld.application.exception.NoMatchedAuthentication;
import io.github.zoooohs.realworld.application.model.AuthenticationRequest;
import io.github.zoooohs.realworld.application.model.RegistrationRequest;
import io.github.zoooohs.realworld.application.model.UpdateUserRequest;
import io.github.zoooohs.realworld.application.model.UsersResponse;
import io.github.zoooohs.realworld.application.port.in.usecase.AuthenticationUseCase;
import io.github.zoooohs.realworld.application.port.out.persistance.user.UserIdGenerator;
import io.github.zoooohs.realworld.application.port.out.persistance.user.UserRepository;
import io.github.zoooohs.realworld.application.port.out.security.PasswordManager;
import io.github.zoooohs.realworld.application.port.out.security.TokenWriter;
import io.github.zoooohs.realworld.domain.model.User;
import io.github.zoooohs.realworld.domain.model.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {
    private final UserRepository userRepository;
    private final UserIdGenerator userIdGenerator;
    private final PasswordManager passwordManager;
    private final TokenWriter tokenWriter;

    @Override
    public UsersResponse registration(RegistrationRequest registrationRequest) {
        String username = registrationRequest.username();
        String email = registrationRequest.email();
        String password = registrationRequest.password();

        if (userRepository.existsByUserName(username)) {
            throw new DuplicatedUsername();
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicatedEmail();
        }

        User user = createNewUser(password, email, username);
        userRepository.save(user);

        return createUsersResponse(user);
    }

    private User createNewUser(String password, String email, String username) {
        UserId userId = userIdGenerator.generate();
        String encryptedPassword = passwordManager.encrypt(password);
        return new User(userId, email, encryptedPassword, username);
    }

    @Override
    public UsersResponse authentication(AuthenticationRequest authenticationRequest) {
        String email = authenticationRequest.email();
        String password = authenticationRequest.password();

        User authenticatedUser = userRepository.findByEmail(email)
                .filter(user -> passwordManager.match(password, user.getPassword()))
                .orElseThrow(NoMatchedAuthentication::new);

        return createUsersResponse(authenticatedUser);
    }

    @Override
    public UsersResponse getCurrentUser(UserId currentUserId) {
        User currentUser = userRepository.getByUserId(currentUserId);
        return createUsersResponse(currentUser);
    }

    @Override
    public UsersResponse updateCurrentUser(UserId currentUserId, UpdateUserRequest updateUserRequest) {
        User currentUser = userRepository.getByUserId(currentUserId);
        changeEmail(updateUserRequest.email(), currentUser);
        changeUsername(updateUserRequest.username(), currentUser);
        changePassword(updateUserRequest.password(), currentUser);
        changeBio(updateUserRequest.bio(), currentUser);
        changeImage(updateUserRequest.image(), currentUser);

        userRepository.save(currentUser);
        return createUsersResponse(currentUser);
    }

    private void changeImage(String image, User currentUser) {
        if (image == null) {
            return;
        }
        currentUser.changeImage(image);
    }

    private void changeBio(String bio, User currentUser) {
        if (bio == null) {
            return;
        }
        currentUser.changeBio(bio);
    }

    private void changePassword(String rawPassword, User currentUser) {
        if (rawPassword == null) {
            return;
        }
        String encryptedPassword = passwordManager.encrypt(rawPassword);
        currentUser.changePassword(encryptedPassword);
    }

    private void changeUsername(String username, User currentUser) {
        if (username == null) {
            return;
        }

        // TODO: 자기 자신 결과도 포함 될텐데, 그 경우도 예외로 던져야 하는가?
        if (userRepository.existsByUserName(username)) {
            throw new DuplicatedUsername();
        }
        currentUser.changeUsername(username);
    }

    private void changeEmail(String email, User currentUser) {
        if (email == null) {
            return;
        }

        // TODO: 자기 자신 결과도 포함 될텐데, 그 경우도 예외로 던져야 하는가?
        if (userRepository.existsByEmail(email)) {
            throw new DuplicatedEmail();
        }
        currentUser.changeEmail(email);
    }

    private UsersResponse createUsersResponse(User user) {
        String token = tokenWriter.issue(user);

        return UsersResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .token(token)
                .build();
    }
}
