package com.namusd.jwtredis.model.dto.video;

import com.namusd.jwtredis.model.dto.AttachFileDto;
import com.namusd.jwtredis.model.dto.UserDto;
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
    }


    @Builder
    @AllArgsConstructor
    @Getter
    @ToString
    public static class Detail {
        private String videoId;
        private String videoTitle;
        private String workTitle;
        private UserDto.Response owner;
        private OriginalFrameDto.Response frameInfo;
        private AttachFileDto.Response videoInfo;
    }
}
