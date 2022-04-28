package io.zoooohs.realworld.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {
    JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils("signKey012345678901234567890123456789", 3000L);
    }

    @ParameterizedTest
    @MethodSource("subs")
    void whenSubIsNotNull_thenReturnJwt_orNull(String sub) {
        String actual = jwtUtils.encode(sub);

        if (sub == null || sub.equals("")) {
            assertNull(actual);
        } else {
            assertNotNull(actual);
        }
    }

    public static Stream<String> subs() {
        return Stream.of("sub1", "sub2", "", null);
    }
}
