package com.namusd.jwtredis.model.dto.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class OriginalFrameDto {

    @Builder
    @AllArgsConstructor
    @ToString
    @Getter
    public static class Response {
        private Long id;
        private int startSequence;
        private int endSequence;
    }
}
