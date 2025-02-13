package com.namusd.jwtredis.model.dto.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

public class MessageDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class KafkaRequestMessage {
        private String url; // input
        @JsonProperty("video_id")
        private String videoId; // 요청 ID
        private String bucket_name;
        private String bucket;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class VideoMetadata {
        private int width;
        private int height;
        private int fps;
        @JsonProperty("total_frames")
        private int totalFrames;
        private double duration;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class KafkaProcessedResponseMessage {
        private String status;
        @JsonProperty("video_id")
        private String videoId;
        @JsonProperty("processed_video_url")
        private String processedVideoUrl;
        @JsonProperty("metadata_url")
        private String metadataUrl;
        private String message;
        @JsonProperty("video_metadata")
        private VideoMetadata videoMetadata;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class KafkaFinalizedResponseMessage {
        private String status;  // 작업 상태
        @JsonProperty("video_id")
        private String videoId; // 요청 ID
        @JsonProperty("finalized_video_url")
        private String finalizedVideoUrl; // 처리 결과 경로
        private String message;
    }

}