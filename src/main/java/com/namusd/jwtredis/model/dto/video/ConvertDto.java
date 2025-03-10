package com.namusd.jwtredis.model.dto.video;

import com.namusd.jwtredis.model.constant.ConvertOperation;
import lombok.*;

public class ConvertDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class Request {
        private String url; // input
        private String requestId; // 요청 ID
        private String bucket_name;
        private String bucket;

        // 새로운 객체를 생성하는 메서드
        public static Request createWithRequestId(String url, String requestId, String bucket) {
            return Request.builder()
                    .url(url)
                    .requestId(requestId)
                    .bucket(bucket)
                    .build();
        }
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class Response {
        private String status;  // 작업 상태
        private String requestId; // 요청 ID
        private String outputPath; // 처리 결과 경로
        @Builder.Default
        private Integer startSequence = 0;
        private Integer endSequence;
        private String videoUrl; // 결과 비디오 URL
        private String message;
    }
}
