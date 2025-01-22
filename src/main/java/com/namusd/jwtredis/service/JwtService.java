package com.namusd.jwtredis.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.namusd.jwtredis.model.constant.JwtProperties;
import com.namusd.jwtredis.model.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    public DecodedJWT decodeRefreshToken(String refreshToken) {
        Algorithm algorithm = Algorithm.HMAC512(JwtProperties.SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(refreshToken);
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

    // 로그인시에 할당된 token id가 없으므로 새로 만들어줘야함
    public String generateRefreshToken(User loginUser, String tokenId, String requestUrl) {
        return JWT.create()
                .withSubject(loginUser.getUsername())
                .withClaim("tokenId", tokenId)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME)) // 2주
                .withIssuer(requestUrl)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public String buildRefreshToken(User loginUser, String tokenId, String requestUrl) {
        return JWT.create()
                .withSubject(loginUser.getUsername())
                .withClaim("tokenId", tokenId)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME)) // 2주
                .withIssuer(requestUrl)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }


}
