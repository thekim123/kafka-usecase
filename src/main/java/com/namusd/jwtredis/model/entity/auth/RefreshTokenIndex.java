package com.namusd.jwtredis.model.entity.auth;

import com.namusd.jwtredis.model.constant.JwtProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@ToString
@RedisHash(value = "refreshTokenIndex", timeToLive = JwtProperties.REFRESH_EXPIRATION_TIME)
public class RefreshTokenIndex {
    @Id
    private String username;
    @Builder.Default
    private Map<String, String> tokenIds = new HashMap<>(); // 토큰ID 맵핑

}
