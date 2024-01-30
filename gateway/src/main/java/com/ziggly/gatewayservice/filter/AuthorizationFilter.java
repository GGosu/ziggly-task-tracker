package com.ziggly.gatewayservice.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;

import java.util.List;

@Component
public class AuthorizationFilter implements WebFilter, Ordered {

    private final WebClient webClient;

    @Autowired
    public AuthorizationFilter(@LoadBalanced WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("lb://auth-service").build();
    }

    private static final List<String> FILTERED_PATHS = List.of(
            "/api/team",
            "/api/projects",
            "/api/tasks",
            "/api/time-entries"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (isResourceServiceRoute(exchange)) {

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String token = authHeader.substring(7); // "Bearer " has 7 characters

            return webClient.get()
                    .uri("/api/auth/validate?token={token}", token)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.ACCEPTED)) {
                            return response.bodyToMono(String.class) // Assuming the body contains the user ID
                                    .flatMap(userId -> {
                                        ServerHttpRequest mutatedRequest = mutateRequest(exchange.getRequest(), userId);
                                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                                    });
                        } else {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                    });


        }
        return chain.filter(exchange);
    }

    private ServerHttpRequest mutateRequest(ServerHttpRequest request, String userId) {
        return new ServerHttpRequestDecorator(request) {
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.addAll(request.getHeaders());
                headers.add("X-USER-ID", userId);
                return headers;
            }
        };
    }

    private boolean isResourceServiceRoute(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().toString();
        return FILTERED_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
