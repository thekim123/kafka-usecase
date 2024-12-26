package com.namusd.jwtredis.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@ToString
@RedisHash(value = "refreshToken", timeToLive = 604800) // 7일(초 단위)
public class RefreshToken {

    @Id
    private String id; // 고유 ID (userId:tokenId)

    private String username;
    private String tokenValue;
    private LocalDateTime createdAt;

    public RefreshToken(String id, String username, String tokenValue) {
        this.id = id;
        this.username = username;
        this.tokenValue = tokenValue;
        this.createdAt = LocalDateTime.now();
    }
}
