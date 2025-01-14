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
@TableName("edited_frame")
public class EditedFrame {
    private Long id;

    private String videoId;
    private int sequence;

    private Long editId;
    private Long editFileId;
}
