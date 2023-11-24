package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.application.port.in.security.TokenReader;
import io.github.zoooohs.realworld.application.port.out.security.TokenWriter;
import io.github.zoooohs.realworld.domain.User;
import io.github.zoooohs.realworld.domain.UserId;

public class FakeTokenWriter implements TokenWriter, TokenReader {
    @Override
    public String issue(User authenticatedUser) {
        return authenticatedUser.getId().toString();
    }

    @Override
    public UserId getUserId(String token) {
        if (token == null) return null;
        try {
            long idValue = Long.parseLong(token);
            return new UserId(idValue);
        } catch (Exception e) {
            return null;
        }
    }
}
