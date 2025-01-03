package com.namusd.jwtredis.model.dto;

import com.namusd.jwtredis.model.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

public class ConvertDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class Request {
        private String url;
        private String operation;
        private String requestId; // 요청 ID

        // 새로운 객체를 생성하는 메서드
        public static Request createWithRequestId(String url, String requestId) {
            return Request.builder()
                    .url(url)
                    .operation("split")
                    .requestId(requestId)
                    .build();
        }
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class Response {
        //        private Long videoUUID;
//        private String title;
//        private String content;
        private String status;
        private String requestId; // 요청 ID
        private String videoOutputPath; // 처리 결과 경로
        private String frameOutputPath; // 처리 결과 경로
        private String operation;
        private String message;


//        private UserDto.Response author;
//        private LocalDateTime createTime;•
//        private LocalDateTime updateTime;

    }
}
