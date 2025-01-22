package com.namusd.jwtredis.model.entity.auth;

import com.namusd.jwtredis.model.constant.JwtProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

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
    private Map<String, String> tokenIds; // 토큰ID 맵핑

}
