package com.namusd.jwtredis.model.entity.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.namusd.jwtredis.model.dto.VideoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
@TableName("video")
public class Video {
    @TableId(value = "video_id", type = IdType.ASSIGN_UUID)
    private String videoId;
    private String videoTitle;
    private Long videoFileId;
    private Long ownerId;

    public VideoDto.Response toDto() {
        return VideoDto.Response.builder()
                .videoId(this.videoId)
                .videoTitle(this.videoTitle)
                .videoFileId(this.videoFileId)
                .ownerId(this.ownerId)
                .build();
    }
}
