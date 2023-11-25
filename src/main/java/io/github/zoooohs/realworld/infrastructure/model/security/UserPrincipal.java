package io.github.zoooohs.realworld.infrastructure.model.security;

import io.github.zoooohs.realworld.domain.model.UserId;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Optional;

@Getter
public class UserPrincipal implements Authentication {
    private final boolean isAuthenticated;
    private final UserId userId;
    private final String username;
    private final String email;

    public static UserPrincipal authenticated(UserId userId, String username, String email) {
        return new UserPrincipal(userId, username, email);
    }

    public static UserId userId(UserPrincipal userPrincipal) {
        return Optional.ofNullable(userPrincipal).map(UserPrincipal::getUserId).orElse(null);
    }

    private UserPrincipal(UserId userId, String username, String email) {
        this.isAuthenticated = true;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
