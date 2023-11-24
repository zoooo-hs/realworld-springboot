package io.github.zoooohs.realworld.domain;

import io.github.zoooohs.realworld.application.port.out.persistance.UserIdGenerator;
import io.github.zoooohs.realworld.fake.FakeUserIdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void newUserGetNonBlankEmailUsernamePassword() {
        UserId userId = newId();
        Assertions.assertThrows(AssertionError.class, () -> { new User(userId, "", "", "");});
    }

    private UserId newId() {
        UserIdGenerator userIdGenerator = new FakeUserIdGenerator(-1L);
        UserId userId = userIdGenerator.generate();
        return userId;
    }

    @Test
    void newUserEmailMustBeEmailFormat() {
        UserId userId = newId();
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> { new User(userId,"notemailform", "hyunsu", "123123");}
        );
    }

    @Test
    void newUser() {
        UserId userId = newId();
       new User(userId, "abc@def.xyz", "hyunsu", "123123") ;

    }

}