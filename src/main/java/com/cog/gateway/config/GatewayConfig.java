package com.cog.gateway.config;

import com.cog.gateway.filter.AdminFilter;
import com.cog.gateway.filter.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthFilter authFilter;
    private final AdminFilter adminFilter;
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("auth-server",r -> r.path("/auth/signup")
                        .uri("http://localhost:2210"))
                .route("auth-server",r -> r.path("/auth/login")
                        .uri("lb://auth-server"))
//                Client Side
//        String username = (String) httpServletRequest.getAttribute("username");
//        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) httpServletRequest.getAttribute("authorities");
                .route("test-service", r -> r.path("/api/v1/admin/test/**")
                        .filters(f -> f.filters(adminFilter))
                        .uri("lb://test-service"))
                .route("test2-service",r -> r.path("/api/v1/test2/**")
                        .filters(f -> f.filters(authFilter))
                        .uri("lb://test2-service"))
                .build();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
