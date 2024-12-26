package com.namusd.jwtredis.config.security;

import com.namusd.jwtredis.persistence.repository.UserRepository;
import com.namusd.jwtredis.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class NamuDsl extends AbstractHttpConfigurer<NamuDsl, HttpSecurity> {
    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    private final JwtService jwtService;

    public NamuDsl(UserRepository userRepository, CorsConfig corsConfig, JwtService jwtService) {
        this.userRepository = userRepository;
        this.corsConfig = corsConfig;
        this.jwtService = jwtService;
    }

    @Override
    public void configure(HttpSecurity builder) {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        builder.addFilter(corsConfig.corsFilter())
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
    }

}
