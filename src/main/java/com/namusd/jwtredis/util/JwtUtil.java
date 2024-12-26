package com.namusd.jwtredis.util;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.namusd.jwtredis.model.constant.JwtProperties;
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletResponse;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtUtil {
    public static void isValidJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return;
        }
        throw new JWTDecodeException("토큰 형식이 올바르지 않습니다.");
    }

    public static void withAccessToken(String accessToken, HttpServletResponse response) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
    }

    public static void withRefreshToken(String refreshToken, HttpServletResponse response) {
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMillis(JwtProperties.REFRESH_EXPIRATION_TIME))
                .build();
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

}
