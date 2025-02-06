package com.namusd.jwtredis.model.vo;

import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import lombok.Builder;

@Builder
public record VideoRegisterVo(String videoId, AttachFile attachFile, String videoFileName) {
}
