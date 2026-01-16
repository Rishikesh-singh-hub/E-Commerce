package com.rishikesh.gateway.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class UserContextFilter implements GlobalFilter {

    Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        logger.info(
                "[GATEWAY-IN] {} {}",
                request.getMethod(),
                request.getURI()
        );

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .flatMap(auth -> {

                    Jwt jwt = auth.getToken();

                    ServerHttpRequest mutatedRequest =
                            exchange.getRequest().mutate()
                                    .header("X-Gateway-Request","true")
                                    .build();

                    return chain.filter(
                            exchange.mutate().request(mutatedRequest).build()
                    ).doOnSubscribe(sub ->
                            logger.info(
                                    "[GATEWAY-FORWARD] Forwarding to {}",
                                    exchange.getRequest().getURI()
                            )
                    );
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}

