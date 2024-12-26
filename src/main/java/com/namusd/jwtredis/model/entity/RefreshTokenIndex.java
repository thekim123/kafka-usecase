package com.namusd.jwtredis.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Map;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@ToString
@RedisHash(value = "refreshTokenIndex", timeToLive = 604800) // 7일 TTL
public class RefreshTokenIndex {

    @Id
    private String username; // 인덱스 키 (userToken:refresh:<username>)
    private Map<String, String> tokenIds; // 토큰ID 맵핑

}
