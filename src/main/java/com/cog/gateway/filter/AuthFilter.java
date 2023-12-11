package com.cog.gateway.filter;

import com.cog.gateway.validator.RouteValidator;
import com.cog.gateway.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RefreshScope
public class AuthFilter implements GatewayFilter {

    @Autowired
    RouteValidator routeValidator;

    @Autowired
    private JWTUtil jwtUtil;



    @Value("${authentication.enabled}")
    private boolean authEnabled;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(!authEnabled) {
            log.info("Authentication is disabled. To enable it, make \"authentication.enabled\" property as true");
            return chain.filter(exchange);
        }
        String token ="";
        ServerHttpRequest request = exchange.getRequest();

        if(routeValidator.isSecured.test(request)) {
            log.info("validating authentication token");
            if(this.isCreedsMissing(request)) {
                log.info("in error");
                return this.onError(exchange);
            }
            else {
                token =request.getHeaders().getOrEmpty("Authorization").get(0).split(" ")[1];
            }

            if(jwtUtil.isInvalid(token)) {
                return this.onError(exchange);
            }
            else {
                log.info("Authentication is successful");
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean isCreedsMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }


}
