package com.namusd.jwtredis.model.dto;

import com.namusd.jwtredis.model.entity.video.FrameStreamDirection;
import lombok.Data;

@Data
public class FrameRequestDto {
    private int start;
    private int end;
    private FrameStreamDirection direction = FrameStreamDirection.BACK;
}
