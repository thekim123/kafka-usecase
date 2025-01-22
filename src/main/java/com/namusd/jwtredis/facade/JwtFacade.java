package com.namusd.jwtredis.facade;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.namusd.jwtredis.config.auth.PrincipalDetails;
import com.namusd.jwtredis.model.entity.auth.RefreshToken;
import com.namusd.jwtredis.model.entity.auth.RefreshTokenIndex;
import com.namusd.jwtredis.model.vo.RefreshTokenVo;
import com.namusd.jwtredis.service.JwtService;
import com.namusd.jwtredis.model.dto.auth.JwtDto;
import com.namusd.jwtredis.model.entity.user.User;
import com.namusd.jwtredis.service.RedisService;
import com.namusd.jwtredis.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtFacade {

    private final JwtService jwtService;
    private final UserService userService;
    private final RedisService redisService;

    public JwtDto.Refresh generateJwt(User loginUser, String requestUrl) {
        // 새로운 토큰 발급
        String access = jwtService.generateAccessToken(loginUser, requestUrl);
        String tokenId = UUID.randomUUID().toString(); // 고유 ID 생성
        String refresh = jwtService.generateRefreshToken(loginUser, tokenId, requestUrl);

        // 발급된 토큰과 토큰 index 를 redis 에 저장
        RefreshToken entity = RefreshToken.builder()
                .id(tokenId)
                .username(loginUser.getUsername())
                .tokenValue(refresh)
                .build();
        redisService.saveRefreshToken(entity);
        redisService.saveRefreshTokenIndex(loginUser.getUsername(), tokenId, refresh);

        return JwtDto.Refresh.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }

    public JwtDto.Refresh refreshJwt(String refreshToken, String requestUrl) {
        // refresh token이 적절한지 검사 시작
        DecodedJWT decodedJWT = jwtService.decodeRefreshToken(refreshToken);
        redisService.validateRefreshToken(refreshToken, decodedJWT);
        RefreshTokenVo vo = redisService.getRefreshTokenInfo(decodedJWT);

        // 새로운 리프레시 토큰 발급
        User loginUser = userService.findByUsername(vo.getUsername());
        String access = jwtService.generateAccessToken(loginUser, requestUrl);
        String refresh = jwtService.buildRefreshToken(loginUser, vo.getTokenId(), requestUrl);

        // 발급된 토큰과 토큰 index 를 redis 에 저장
        RefreshToken entity = RefreshToken.builder()
                .id(vo.getTokenId())
                .username(loginUser.getUsername())
                .tokenValue(refresh)
                .build();
        redisService.saveRefreshToken(entity);
        redisService.saveRefreshTokenIndex(loginUser.getUsername(), vo.getTokenId(), refreshToken);

        return JwtDto.Refresh.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }


    public void logout(String refreshToken) {
        // refresh token이 적절한지 검사 시작
        DecodedJWT decodedJWT = jwtService.decodeRefreshToken(refreshToken);
        redisService.validateRefreshToken(refreshToken, decodedJWT);
        RefreshTokenVo vo = redisService.getRefreshTokenInfo(decodedJWT);

        redisService.deleteRefreshToken(vo.getTokenId());
        redisService.deleteRefreshTokenIndex(vo.getUsername(), vo.getTokenId());
    }

    public void logoutDevice(Authentication auth, String deviceId) {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();
        // refresh token이 적절한지 검사 시작
        redisService.deleteRefreshToken(deviceId);
        redisService.deleteRefreshTokenIndex(loginUser.getUsername(), deviceId);
    }

    public void logoutAll(Authentication auth) {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();
        RefreshTokenIndex tokenIndex = redisService.getUserTokenIndex(loginUser.getUsername());

        if (tokenIndex == null) {
            return;
        }

        redisService.invalidateTokens(tokenIndex);
        redisService.deleteTokenIndex(loginUser.getUsername());
    }


}
