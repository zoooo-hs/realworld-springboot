package io.zoooohs.realworld.security;

import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    /**
     * TODO:
     * userDetailService -> authority 를 찾는 용도로만 사용하는게 좋을 것 같음 authentication에는 userid만 들어가도록하고, authority만따로 보내도록
     *  --> UserEntity에 UserDetail내용 빼기
     */
    public Authentication getAuthentication(String username) {
        UserDetails userDetail = userDetailsService.loadUserByUsername(username);
        if (userDetail == null) return null;
        UserEntity userEntity = (UserEntity) userDetail;
        UserDto.Auth authenticatedUser = UserDto.Auth.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .bio(userEntity.getBio())
                .image(userEntity.getImage())
                .build();
        return new UsernamePasswordAuthenticationToken(authenticatedUser, "", userDetail.getAuthorities());
    }
}
