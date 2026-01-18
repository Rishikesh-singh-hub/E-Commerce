package com.rishikesh.product.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Security filter chain started......");
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/api/products/auth","/api/products/auth/**").authenticated()
                                .requestMatchers("/api/products/public/**").permitAll()
                                .requestMatchers("/api/seller/add-seller").authenticated()
                                .anyRequest().denyAll()
                ).oauth2ResourceServer(oauth -> oauth.jwt(
                        jwt -> jwt
                                .jwkSetUri("http://localhost:8080/auth/.well-known/jwks.json")
                ));

        return http.build();
    }

    @Bean
    public WebClient.Builder webClient(){
        return  WebClient.builder();
    }

}
