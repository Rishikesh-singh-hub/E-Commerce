package com.rishikesh.gateway.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @PostConstruct
    void loaded() {
        System.out.println(">>> GATEWAY SECURITY CONFIG LOADED <<<");
    }

    @Bean
    public SecurityWebFilterChain securityChain(ServerHttpSecurity http){
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/user/signing",
                                "/auth/.well-known/jwks.json",
                                "/api/user/signup",
                                "/api/user/verify-email",
                                "/api/user/verify-otp")
                                .permitAll()
                                .anyExchange()
                                .authenticated()
                        ).oauth2ResourceServer(oauth -> oauth
                        .jwt(jwtSpec -> jwtSpec.jwtDecoder(jwtDecoder()) )
                ).build();

    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {

        NimbusReactiveJwtDecoder decoder =
                NimbusReactiveJwtDecoder.withJwkSetUri(
                        "http://localhost:8080/auth/.well-known/jwks.json"
                ).build();

        OAuth2TokenValidator<Jwt> withIssuer =
                JwtValidators.createDefaultWithIssuer("user-service");

        OAuth2TokenValidator<Jwt> withAudience =
                token -> {
                    if (token.getAudience().contains("api-gateway")) {
                        return OAuth2TokenValidatorResult.success();
                    }
                    return OAuth2TokenValidatorResult.failure(
                            new OAuth2Error("invalid_token", "Invalid audience", null)
                    );
                };

        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience)
        );

        return decoder;
    }

}
