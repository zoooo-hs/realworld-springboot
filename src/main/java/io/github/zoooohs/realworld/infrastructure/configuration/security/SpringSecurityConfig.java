package io.github.zoooohs.realworld.infrastructure.configuration.security;

import io.github.zoooohs.realworld.application.port.in.security.TokenReader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final TokenReader tokenReader;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .authorizeHttpRequests( request ->
                        request
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/api/users/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/profiles/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(tokenReader), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
