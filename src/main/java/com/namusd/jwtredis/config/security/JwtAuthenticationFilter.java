package com.namusd.jwtredis.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namusd.jwtredis.config.auth.PrincipalDetails;
import com.namusd.jwtredis.facade.JwtFacade;
import com.namusd.jwtredis.model.dto.JwtDto;
import com.namusd.jwtredis.model.dto.LoginRequestDto;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.service.JwtService;
import com.namusd.jwtredis.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtFacade jwtFacade;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            JwtFacade jwtFacade
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtFacade = jwtFacade;
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
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication auth
    ) throws IOException {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();
        String requestUrl = request.getRequestURL().toString();

        JwtDto.Refresh tokens = jwtFacade.generateJwt(loginUser, requestUrl);
        JwtUtil.withRefreshToken(tokens.getRefreshToken(), response);
        Map<String, String> access = new HashMap<>();
        access.put("access", tokens.getAccessToken());
        new ObjectMapper().writeValue(response.getOutputStream(), ResponseEntity.status(HttpStatus.OK).body(access));
    }

}
