package io.zoooohs.realworld.domain.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.zoooohs.realworld.configuration.WithAuthUser;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import io.zoooohs.realworld.domain.user.service.UserService;
import io.zoooohs.realworld.security.JWTAuthFilter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JWTAuthFilter jwtAuthFilter;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    UserService userService;

    @Test
    @WithAuthUser
    void whenAuthorizedUser_returnUserDto() throws Exception {
        UserDto result = UserDto.builder().email("email@email.com").name("username").build();

        when(userService.currentUser(any(UserDto.Auth.class))).thenReturn(result);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(result.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username", Matchers.is(result.getName())));
    }

    @Test
    @WithAuthUser
    void whenUpdateDto_returnUpdatedUserDto() throws Exception {
        UserDto.Update update = UserDto.Update.builder().name("newName").bio("newBio").build();
        UserDto result = UserDto.builder().name("newName").bio("newBio").build();

        when(userService.update(any(UserDto.Update.class), any(UserDto.Auth.class))).thenReturn(result);

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username", Matchers.is(update.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.bio", Matchers.is(update.getBio())));
    }
}
