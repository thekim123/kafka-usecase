package com.namusd.jwtredis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class RefreshTokenVo {
    String tokenId;
    String username;
    String refreshToken;
}
