package com.namusd.jwtredis.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.namusd.jwtredis.handler.ex.InvalidRefreshTokenException;
import com.namusd.jwtredis.model.entity.auth.RefreshToken;
import com.namusd.jwtredis.model.entity.auth.RefreshTokenIndex;
import com.namusd.jwtredis.model.vo.RefreshTokenVo;
import com.namusd.jwtredis.repository.RefreshTokenIndexRepository;
import com.namusd.jwtredis.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final TokenRepository refreshTokenRepository;
    private final RefreshTokenIndexRepository tokenIndexRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 리프레시 토큰 저장
     */
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    // RefreshTokenIndex 업데이트
    public void saveRefreshTokenIndex(String username, String tokenId, String tokenValue) {
        Optional<RefreshTokenIndex> indexOpt = tokenIndexRepository.findById(username);

        RefreshTokenIndex index;
        index = indexOpt.orElseGet(() -> new RefreshTokenIndex(username, new HashMap<>()));

        index.getTokenIds().put(tokenId, tokenValue);
        tokenIndexRepository.save(index);
    }

    /**
     * 특정 사용자 모든 토큰 조회
     */
    public List<RefreshToken> getRefreshTokensByUsername(String username) {
        return refreshTokenRepository.findByUsername(username);
    }

    public void validateRefreshToken(String tokenValue, DecodedJWT decodedJWT) {
        String tokenId = decodedJWT.getClaim("tokenId").asString();

        RefreshToken refreshToken = refreshTokenRepository.findById(tokenId)
                .orElseThrow(() -> new AccessDeniedException("리프레시 토큰이 존재하지 않습니다."));

        if (!refreshToken.getTokenValue().equals(tokenValue)) {
            throw new InvalidRefreshTokenException("리프레시 토큰 값이 일치하지 않습니다.");
        }
    }

    public RefreshTokenVo getRefreshTokenInfo(DecodedJWT decodedJWT) {
        String tokenId = decodedJWT.getClaim("tokenId").asString();
        String username = decodedJWT.getSubject();

        return RefreshTokenVo.builder()
                .tokenId(tokenId)
                .username(username)
                .refreshToken(decodedJWT.getToken())
                .build();
    }


    public void deleteRefreshToken(String tokenId) {
        refreshTokenRepository.deleteById(tokenId);
    }

    public void deleteRefreshTokenIndex(String username, String tokenId) {
        Optional<RefreshTokenIndex> indexOpt = tokenIndexRepository.findById(username);
        RefreshTokenIndex index;
        index = indexOpt.orElseGet(() -> new RefreshTokenIndex(username, new HashMap<>()));
        index.getTokenIds().remove(tokenId);
        tokenIndexRepository.save(index);
    }

    // 사용자 토큰 인덱스를 조회
    public RefreshTokenIndex getUserTokenIndex(String username) {
        return tokenIndexRepository.findById(username).orElseThrow(()->new RuntimeException(" 아몰랑"));
    }

    // 모든 토큰 삭제
    public void invalidateTokens(RefreshTokenIndex tokenIndex) {
        if (tokenIndex == null || tokenIndex.getTokenIds() == null) {
            return;
        }

        for (String tokenId : tokenIndex.getTokenIds().keySet()) {
            redisTemplate.delete("refreshToken:" + tokenId); // 각 토큰 삭제
        }
    }

    // 토큰 인덱스 삭제
    public void deleteTokenIndex(String username) {
        redisTemplate.delete("refreshTokenIndex:" + username);
    }


}
