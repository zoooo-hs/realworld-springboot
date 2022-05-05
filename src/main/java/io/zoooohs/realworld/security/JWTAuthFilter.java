package io.zoooohs.realworld.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends GenericFilter {
    private final JwtUtils jwtUtils;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = jwtUtils.resolveToken(request);
        if (jwt != null && jwtUtils.validateToken(jwt)) {
            String username = jwtUtils.getSub(jwt);
            Authentication authentication = authenticationProvider.getAuthentication(username);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
