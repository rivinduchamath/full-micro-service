package com.cog.gateway.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> unprotectedURLs = List.of("/auth/login","/auth/signup");

    public Predicate<ServerHttpRequest> isSecured = new Predicate<ServerHttpRequest>() {
        @Override
        public boolean test(ServerHttpRequest request) {
            for (String uri : unprotectedURLs) {
                if (request.getURI().getPath().contains(uri)) {
                    return false;
                }
            }
            return true;
        }
    };
}
