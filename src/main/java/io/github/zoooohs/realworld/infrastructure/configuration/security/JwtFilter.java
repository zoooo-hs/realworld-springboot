package io.github.zoooohs.realworld.infrastructure.configuration.security;

import io.github.zoooohs.realworld.application.model.user.TokenPayload;
import io.github.zoooohs.realworld.application.port.in.security.TokenReader;
import io.github.zoooohs.realworld.infrastructure.model.security.UserPrincipal;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilter {
    private final String JWT_PREFIX = "TOKEN ";
    private final String JWT_HEADER = HttpHeaders.AUTHORIZATION;
    private final TokenReader tokenReader;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Optional.ofNullable(httpRequest)
                .map(req -> req.getHeader(JWT_HEADER))
                .map(authorization -> authorization.substring(JWT_PREFIX.length()))
                .map(tokenReader::read)
                .map(this::tokenPayloadToAuthentication)
                .ifPresent(SecurityContextHolder.getContext()::setAuthentication);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken tokenPayloadToAuthentication(TokenPayload tokenPayload) {
        Authentication authentication;
        authentication = UserPrincipal.authenticated(
                tokenPayload.userId(),
                tokenPayload.username(),
                tokenPayload.email()
        );
        return new UsernamePasswordAuthenticationToken(authentication, "", null);
    }
}
