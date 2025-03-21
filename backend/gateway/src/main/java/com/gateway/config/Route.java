package com.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Route {
    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return org.springframework.web.reactive.function.server.RouterFunctions.route(
                RequestPredicates.GET("/fallbackRoute"),
                request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .bodyValue("Service Unavailable, please try again later")
        );
    }
}
