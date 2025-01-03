package com.namusd.jwtredis.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
//        config.setAllowedHeaders(Arrays.asList("*"));

        config.setAllowedHeaders(
                Arrays.asList(
                        "X-Requested-With",
                        "Origin",
                        "Content-Type",
                        "Accept",
                        "Authorization",
                        "strict-origin-when-cross-origin"
                ));
        config.setExposedHeaders(
                Arrays.asList(
                        "Access-Control-Allow-Headers",
                        "Authorization, x-xsrf-token",
                        "Access-Control-Allow-Headers", "Origin", "Accept", "X-Requested-With",
                        "Content-Type", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                ));
        config.addAllowedOrigin("http://localhost:3000");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
