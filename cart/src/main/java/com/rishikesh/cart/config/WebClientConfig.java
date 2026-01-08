package com.rishikesh.cart.config;

import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient productClient(WebClient.Builder builder,
                                   @Value("${services.product.base-url}") String productBaseUrl) {
        return builder
                .baseUrl(productBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Bean
    public WebClient userClient(WebClient.Builder builder,
                                @Value("${services.user.base-url}") String userBaseUrl) {
        return builder
                .baseUrl(userBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    @Bean
    public WebClient.Builder builder() {
        return WebClient.builder();
    }

}
