package com.namusd.jwtredis.model.entity.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
// MinIO 디렉토리는 videoId가 디렉토리가 됨.
public class OriginalFrame {
    @TableId(value = "original_frame_id", type = IdType.AUTO)
    private Long id;
    private String videoId;
    private int startSequence;
    private int endSequence;
}
