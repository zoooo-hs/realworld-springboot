package io.zoooohs.realworld.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.zoooohs.realworld.domain.user.dto.UserDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class SecurityBootAppTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void registration_and_login_return_tokens_and_can_get_current_user() throws Exception {
        // Registration
        UserDto.Registration registration = UserDto.Registration.builder().email("test@test.com").username("testman").password("password").build();
        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", Matchers.is(registration.getEmail())))
                .andExpect(jsonPath("$.user.username", Matchers.is(registration.getUsername())))
                .andExpect(jsonPath("$.user.token", Matchers.notNullValue()))
                .andReturn();
        String accessToken = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.user.token");

        // After Registration get current user
        mockMvc.perform(get("/user")
                        .header(HttpHeaders.AUTHORIZATION, addJwt(accessToken))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(registration.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username", Matchers.is(registration.getUsername())));

        // Login
        UserDto.Login login = UserDto.Login.builder().email(registration.getEmail()).password(registration.getPassword()).build();
        mvcResult = mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(login.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.token", Matchers.notNullValue()))
                .andReturn();
        accessToken = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.user.token");

        // After Login get current user
        mockMvc.perform(get("/user")
                        .header(HttpHeaders.AUTHORIZATION, addJwt(accessToken))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", Matchers.is(registration.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username", Matchers.is(registration.getUsername())));
    }

    private String addJwt(String accessToken) {
        return "Token " + accessToken;
    }

}
