package com.namusd.jwtredis.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namusd.jwtredis.config.auth.PrincipalDetails;
import com.namusd.jwtredis.model.dto.LoginRequestDto;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.service.JwtService;
import com.namusd.jwtredis.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        setFilterProcessesUrl("/auth/login"); // /login 엔드포인트로 요청 처리
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper om = new ObjectMapper();
        LoginRequestDto dto;
        try {
            dto = om.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            log.error("Failed to attemptAuthentication", e);
            throw new UsernameNotFoundException("Failed to parse login request", e);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();
        String requestUrl = request.getRequestURL().toString();

        String jwtToken = jwtService.generateAccessToken(loginUser, requestUrl);
        JwtUtil.withAccessToken(jwtToken, response);
        String refresh = jwtService.generateRefreshToken(loginUser, requestUrl);
        JwtUtil.withRefreshToken(refresh, response);
    }

}
