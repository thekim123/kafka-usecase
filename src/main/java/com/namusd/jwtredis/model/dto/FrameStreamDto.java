package com.namusd.jwtredis.model.dto;

import com.namusd.jwtredis.model.entity.video.FrameStreamDirection;
import lombok.*;

public class FrameStreamDto {
    @Builder
    @AllArgsConstructor
    @Getter
    @ToString
    public static class Response {
        private FrameStreamDirection direction;
        private String base64Data;
        private Integer sequence;
    }
}
