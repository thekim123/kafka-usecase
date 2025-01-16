package com.namusd.jwtredis.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class VideoDto {

    @Builder
    @AllArgsConstructor
    @Getter
    @ToString
    public static class Response {
        private String videoId;
        private String videoTitle;
        private Long videoFileId;
        private Long ownerId;
    }
}
