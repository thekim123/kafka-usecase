package com.namusd.jwtredis.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.namusd.jwtredis.model.constant.JwtProperties;
import com.namusd.jwtredis.model.entity.RefreshToken;
import com.namusd.jwtredis.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RedisService redisService;

    public String verifyJwtAndGetUsername(String token) {
        String removePrefix = token.replace(JwtProperties.TOKEN_PREFIX, "");
        String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(removePrefix)
                .getClaim("username").asString();
        if (username == null) {
            throw new TokenExpiredException("비정상적인 토큰입니다. 다시 로그인해주십시오.");
        }

        return username;
    }

    public DecodedJWT decodeRefreshToken(String refreshToken) {
        String refresh = refreshToken.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC512(JwtProperties.SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(refresh);
    }


    public String generateAccessToken(User loginUser, String requestUrl) {
        return JWT.create()
                .withSubject(loginUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", loginUser.getId())
                .withClaim("username", loginUser.getUsername())
                .withClaim("roles", loginUser.getRole().toString())
                .withIssuer(requestUrl)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public String generateRefreshToken(User loginUser, String requestUrl) {
        String tokenId = UUID.randomUUID().toString(); // 고유 ID 생성
        tokenId = loginUser.getUsername() + ":" + tokenId;
        return buildRefreshToken(loginUser, tokenId, requestUrl);
    }

    public String updateRefreshToken(User loginUser, String tokenId, String requestUrl) {
        return buildRefreshToken(loginUser, tokenId, requestUrl);
    }

    private String buildRefreshToken(User loginUser, String tokenId, String requestUrl) {
        String refreshToken = JWT.create()
                .withSubject(loginUser.getUsername())
                .withClaim("tokenId", tokenId)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME)) // 2주
                .withIssuer(requestUrl)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

//        RefreshToken tokenEntity = redisService.getRefreshToken(tokenId).orElseThrow(() -> new TokenExpiredException("expire!"));
        saveRefreshToken(loginUser, tokenId, refreshToken);
        redisService.saveRefreshTokenIndex(loginUser.getUsername(), tokenId);
        return refreshToken;
    }

    private void saveRefreshToken(User loginUser, String tokenId, String refreshToken) {
        RefreshToken tokenEntity = RefreshToken.builder()
                .id(tokenId)
                .tokenValue(refreshToken)
                .username(loginUser.getUsername())
                .build();
        System.out.println(tokenId);
        redisService.saveRefreshToken(tokenEntity);
    }

}
