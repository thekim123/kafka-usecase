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
@TableName("video_edit")
public class VideoEdit {
    private int id;
    private String videoId;
    private String editTitle;
}
