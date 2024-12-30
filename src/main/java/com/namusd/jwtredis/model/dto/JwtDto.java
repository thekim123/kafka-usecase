package com.namusd.jwtredis.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

public class JwtDto {

    @AllArgsConstructor
    @Builder
    @Getter
    public static class Refresh {
        @JsonProperty("accessToken")
        private String accessToken;
        @JsonProperty("refreshToken")
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class RefreshRequest {
        @JsonProperty("sendRefreshToken")
        private String sendRefreshToken;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class DeviceRemoteLogout {
        @JsonProperty("deviceId")
        private String deviceId;
    }
}
