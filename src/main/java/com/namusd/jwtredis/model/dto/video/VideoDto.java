package com.namusd.jwtredis.model.dto.video;

import com.namusd.jwtredis.model.dto.AttachFileDto;
import com.namusd.jwtredis.model.dto.UserDto;
import com.namusd.jwtredis.model.entity.video.VideoStatus;
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
        private String workTitle;
        private VideoStatus videoStatus;
    }


    @Builder
    @AllArgsConstructor
    @Getter
    @ToString
    public static class Detail {
        private String videoId;
        private String videoTitle;
        private String workTitle;
        private Double duration;
        private Integer totalFrameCount;
        private UserDto.Response owner;
        private AttachFileDto.Response videoInfo;
        private int fps;
    }
}
