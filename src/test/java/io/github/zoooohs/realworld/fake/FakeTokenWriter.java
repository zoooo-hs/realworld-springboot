package io.github.zoooohs.realworld.fake;

import io.github.zoooohs.realworld.application.model.TokenPayload;
import io.github.zoooohs.realworld.application.port.in.security.TokenReader;
import io.github.zoooohs.realworld.application.port.out.security.TokenWriter;
import io.github.zoooohs.realworld.domain.model.User;
import io.github.zoooohs.realworld.domain.model.UserId;

public class FakeTokenWriter implements TokenWriter, TokenReader {
    @Override
    public String issue(User authenticatedUser) {
        return authenticatedUser.getId().toString();
    }

    @Override
    public TokenPayload read(String token) {
        if (token == null) return null;
        return new TokenPayload(new UserId(token), "", "");
    }
}
