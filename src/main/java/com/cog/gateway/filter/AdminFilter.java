package com.cog.gateway.filter;

import com.cog.gateway.util.JWTUtil;
import com.cog.gateway.validator.RouteValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminFilter implements GatewayFilter {

    private final RouteValidator routeValidator;
    private final JWTUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if(routeValidator.isSecured.test(request)) {
            if (authMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            final String token = request.getHeaders().getOrEmpty("Authorization").get(0).split(" ")[1];

            if(jwtUtil.isInvalid(token)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            List<Map<String, Object>> authorities;
            authorities = (List<Map<String, Object>>) jwtUtil.getALlClaims(token).get("Authorities");

            if (!authorities.get(0).containsKey("authority") || !authorities.get(0).get("authority").equals("ADMIN")) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
