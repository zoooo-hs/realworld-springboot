package io.zoooohs.realworld.configuration;

import io.zoooohs.realworld.security.AuthUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        String email = annotation.email();
        Long id = annotation.id();

        AuthUserDetails authUserDetails = AuthUserDetails.builder().id(id).email(email).build();
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(authUserDetails, "", null);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}
