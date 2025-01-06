package com.namusd.jwtredis.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class FrameUpdateDto {

    /**
     * ModelAttribute를 사용하기 위해 setter 넣음
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class PutRequest {
        MultipartFile file;
        Long id;
    }


}
