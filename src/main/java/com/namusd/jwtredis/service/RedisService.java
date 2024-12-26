package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.entity.RefreshToken;
import com.namusd.jwtredis.model.entity.RefreshTokenIndex;
import com.namusd.jwtredis.persistence.repository.RefreshTokenIndexRepository;
import com.namusd.jwtredis.persistence.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final TokenRepository refreshTokenRepository;
    private final RefreshTokenIndexRepository tokenIndexRepository;

    /**
     * 리프레시 토큰 저장
     */
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public void saveRefreshTokenIndex(String username, String tokenId) {
        // Step 2: RefreshTokenIndex 업데이트
        Optional<RefreshTokenIndex> indexOpt = tokenIndexRepository.findByUsername(username);

        RefreshTokenIndex index;
        index = indexOpt.orElseGet(() -> new RefreshTokenIndex(username, new HashMap<>()));

        index.getTokenIds().put(tokenId, tokenId);
        tokenIndexRepository.save(index);
    }

    /**
     * 특정 사용자 모든 토큰 조회
     */
    public List<RefreshToken> getRefreshTokensByUsername(String username) {
        return refreshTokenRepository.findByUsername(username);
    }

    /**
     * 특정 토큰 조회
     */
    public Optional<RefreshToken> getRefreshToken(String tokenId) {
        return refreshTokenRepository.findById(tokenId);
    }

    /**
     * 특정 토큰 삭제
     */
    public void deleteRefreshToken(String id) {
        refreshTokenRepository.deleteById(id);
    }

    /**
     * 사용자 모든 토큰 삭제
     */
    public void deleteAllTokensByUserId(String userId) {
        List<RefreshToken> tokens = getRefreshTokensByUsername(userId);
        tokens.forEach(token -> refreshTokenRepository.deleteById(token.getId()));
    }
}
