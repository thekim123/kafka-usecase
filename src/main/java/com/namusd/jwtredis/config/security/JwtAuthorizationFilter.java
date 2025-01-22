package com.namusd.jwtredis.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.namusd.jwtredis.config.auth.PrincipalDetails;
import com.namusd.jwtredis.handler.ex.EntityNotFoundException;
import com.namusd.jwtredis.model.constant.JwtProperties;
import com.namusd.jwtredis.model.entity.user.User;
import com.namusd.jwtredis.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(
            AuthenticationManager authenticationManager,
            UserRepository userRepository
    ) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
//            log.info("token prefix is null or not start with Bearer.");
//            log.info("request uri: {}", request.getRequestURI());
//            log.info("request : {}", request.getRemoteAddr());
            return;
        }
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        try {
            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                    .build().verify(token)
                    .getClaim("username").asString();

            if (username != null) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

                PrincipalDetails principalDetails = new PrincipalDetails(user);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                principalDetails,
                                null,
                                principalDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenExpiredException e) {
            log.error("Token is expired or invalid.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status to 403 Forbidden
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Token is expired or invalid.\"}");
            return; // Prevent further filter chain execution
        }


        chain.doFilter(request, response);
    }


}
