package com.ziggly.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/api/auth/**")
                        .uri("lb://auth-service"))
                .route(p -> p.path("/api/profile/**")
                        .uri("lb://auth-service"))
                .route(p -> p.path("/api/team/**")
                        .uri("lb://team-service"))
                .route(p -> p.path("/api/projects/**")
                        .uri("lb://project-service"))
                .route(p -> p.path("/api/tasks/**")
                        .uri("lb://task-service"))
                .route(p -> p.path("/api/time-entries/**")
                        .uri("lb://time-service"))
                .build();
    }
}

