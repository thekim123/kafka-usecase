package com.namusd.jwtredis.model.vo;

import com.namusd.jwtredis.model.entity.AttachFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
public record VideoRegisterVo(String videoId, AttachFile attachFile, String videoFileName) {
}
