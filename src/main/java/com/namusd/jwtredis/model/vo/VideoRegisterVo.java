package com.namusd.jwtredis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class VideoRegisterVo {
    private final String videoId;
    private final Long attachFileId;
    private final String videoFileName;
}
