package com.namusd.jwtredis.config.security;

import com.namusd.jwtredis.facade.JwtFacade;
import com.namusd.jwtredis.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class NamuDsl extends AbstractHttpConfigurer<NamuDsl, HttpSecurity> {
    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    private final JwtFacade jwtFacade;

    public NamuDsl(UserRepository userRepository, CorsConfig corsConfig, JwtFacade jwtFacade) {
        this.userRepository = userRepository;
        this.corsConfig = corsConfig;
        this.jwtFacade = jwtFacade;
    }

    @Override
    public void configure(HttpSecurity builder) {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        builder.addFilter(corsConfig.corsFilter())
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtFacade))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
    }

}
