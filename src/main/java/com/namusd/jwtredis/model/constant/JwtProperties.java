package com.namusd.jwtredis.model.constant;


/**
 * @apiNote interface -> class 로 변경
 * @since 2024.01.05
 */
public class JwtProperties {
    public static final String SECRET = "namusd";

    // 15분
    public static final Long ACCESS_EXPIRATION_TIME = 15 * 60 * 1000L;

    // 2주
    public static final Long REFRESH_EXPIRATION_TIME = 14 * 24 * 60 * 60 * 1000L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
