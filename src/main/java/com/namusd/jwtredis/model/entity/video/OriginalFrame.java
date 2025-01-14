package com.namusd.jwtredis.model.entity.video;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@TableName("original_frame")
public class OriginalFrame {
    private String videoId;
    private int sequence;
    private String frameFileId;
}
