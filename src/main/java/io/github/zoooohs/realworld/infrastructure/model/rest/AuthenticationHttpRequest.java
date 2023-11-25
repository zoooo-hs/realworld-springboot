package io.github.zoooohs.realworld.infrastructure.model.rest;

import io.github.zoooohs.realworld.application.model.AuthenticationRequest;

public record AuthenticationHttpRequest(AuthenticationRequest user) {
}
