package com.rishikesh.user.configuration;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Security filter chain started");
         http.csrf(AbstractHttpConfigurer::disable)
              .cors(Customizer.withDefaults())
              .sessionManagement(session ->
                      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              )
                .authorizeHttpRequests(
                        req -> req

                                .anyRequest().permitAll()
                );

                return http.build();
    }

}
