package com.rishikesh.app.configuration;

import com.rishikesh.app.jwt.AuthTokenFilter;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final AuthTokenFilter authTokenFilter;

    public SecurityConfig(AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
    }

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
                                .requestMatchers("/api/signing","/api/signup").permitAll()
                                .requestMatchers("/api/products/auth","/api/products/auth/**").authenticated()
                                .requestMatchers("/api/products/public/**").permitAll()
                                .requestMatchers("/api/cart/**","/api/cart").authenticated()
                                .requestMatchers("/api/auth").authenticated()
                                .requestMatchers("/api/order","/api/order/**").authenticated()
                                .anyRequest().authenticated()
                );
                http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
    }

}
