package com.namusd.jwtredis.model.dto.video;

import lombok.*;

public class TimelineDto {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class KafkaResponseMessage {
        private String videoId;
        private String message;
        private int fps;
        private int totalFrameCount;

    }
}
