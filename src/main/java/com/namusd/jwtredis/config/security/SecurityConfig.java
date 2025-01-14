package com.namusd.jwtredis.config.security;

import com.namusd.jwtredis.facade.JwtFacade;
import com.namusd.jwtredis.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    private final JwtFacade jwtFacade;

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new NamuDsl(userRepository, corsConfig, jwtFacade))
                .and()
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/admin/**").hasRole("ADMIN")
                        .antMatchers("/swagger-ui.html").hasAnyRole("USER", "ADMIN")
                        .antMatchers("/api/bookmark/**").hasAnyRole("USER", "GUEST", "ADMIN")
                        .antMatchers("/stream/test","/api/user/join", "/chat/**", "/api/region/**", "/api/gati/**").permitAll()
                        .anyRequest().permitAll())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
