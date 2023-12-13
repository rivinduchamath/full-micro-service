package com.cog.gateway.config.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Primary
@Configuration
public class SwaggerConfig implements SwaggerResourcesProvider {
    public static final String API_URI = "/v3/api-docs";
    private final RouteDefinitionLocator routeLocator;

    public SwaggerConfig(RouteDefinitionLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("1.0");
        return swaggerResource;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        routeLocator.getRouteDefinitions().subscribe(
                routeDefinition -> {
                    String resourceName = routeDefinition.getId();
                    log.info(routeDefinition.toString());
                    resources.add(
                            swaggerResource(resourceName, "/api/" + resourceName.replace("-SERVICE", "").toLowerCase() + API_URI)
                    );
                }
        );
        return resources;
    }


}
