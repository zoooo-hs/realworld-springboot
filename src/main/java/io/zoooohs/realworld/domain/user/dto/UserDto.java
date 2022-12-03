package io.zoooohs.realworld.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
@AllArgsConstructor
@Builder
@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UserDto {
    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;

    @Getter
    @AllArgsConstructor
    @Builder
    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class Registration {
        @NotNull
        @Pattern(regexp = "[\\w\\d]{1,30}", message = "string contains alphabet or digit with length 1 to 30")
        private String username;

        @NotNull
        @Email
        private String email;

        @NotBlank
        @Size(min = 8, max = 32)
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class Login {
        @NotNull
        @Email
        private String email;

        @NotBlank
        @Size(min = 8, max = 32)
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class Update {
        private Long id;
        private String email;
        private String username;
        private String bio;
        private String image;
        private String password;
    }

}
