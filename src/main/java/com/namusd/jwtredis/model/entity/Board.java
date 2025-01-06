package com.namusd.jwtredis.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.namusd.jwtredis.model.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@TableName(value = "board")
public class Board {
    @TableId(value = "id", type = IdType.AUTO) // AUTO_INCREMENT 사용
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BoardDto.Response toDto() {
        return BoardDto.Response.builder()
                .id(this.id)
                .content(this.content)
                .title(this.title)
                .createTime(this.createdAt)
                .updateTime(this.updatedAt)
                .build();
    }

}
