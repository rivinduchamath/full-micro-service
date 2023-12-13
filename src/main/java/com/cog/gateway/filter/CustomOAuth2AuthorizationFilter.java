package com.cog.gateway.filter;


import com.cog.gateway.config.auth.AuthConfig;
import com.cog.gateway.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class CustomOAuth2AuthorizationFilter extends AbstractGatewayFilterFactory<AuthConfig> {


    private final JWTUtil jwtUtil;


    public CustomOAuth2AuthorizationFilter( JWTUtil jwtUtil) {
        super(AuthConfig.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(AuthConfig authConfig) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.mutate().build().getRequest();
              if (request.getPath().toString().contains("/auth/user/verify")
                      || request.getPath().toString().contains("/auth/login")
                    || request.getPath().toString().contains("/oauth2/authorize/facebook")
                    || request.getPath().toString().contains("/oauth2/authorize/google")
                    || request.getPath().toString().contains("/oauth2/authorize/github")
                    || request.getPath().toString().contains("/v3/api-docs")) {
                log.info("LOG::Swagger ui loading and token generate" + request.getId());
                return chain.filter(exchange);
            }
          else if (request.getPath().toString().contains("/auth/signup")) {
              log.info("LOG::Sign Up");
              return chain.filter(exchange);
          }
          else {
                String accessToken = Objects.requireNonNull(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION)).substring("Bearer ".length());
                if (checkToken(accessToken)) {
                    // @TODO Implement Api data
                    return null;
                } else {
                    return this.errorResponse(exchange);
                }
            }
        };

    }

    protected Mono<Void> errorResponse(ServerWebExchange response) {
        ServerHttpResponse response1 = response.getResponse();
        response1.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response1.setStatusCode(HttpStatus.FORBIDDEN);
        DataBuffer dataBuffer = response1.bufferFactory().wrap("cannot authorize".getBytes());
        return response1.writeWith(Mono.just(dataBuffer));
    }

    private boolean checkToken(String token) {


        if (jwtUtil.isInvalid(token)) {
            return false;
        } else {
            log.info("Authentication is successful");
            return true;
        }

    }

}
