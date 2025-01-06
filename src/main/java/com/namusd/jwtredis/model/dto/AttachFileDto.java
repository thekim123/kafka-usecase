package com.namusd.jwtredis.model.dto;

import lombok.*;

import java.time.LocalDateTime;

public class AttachFileDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class Response{
        private Long id;
        private String filePath;
        private String fileKey;
        private String fileName;
        private String fileDir;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
