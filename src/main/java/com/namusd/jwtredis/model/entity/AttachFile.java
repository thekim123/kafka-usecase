package com.namusd.jwtredis.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.namusd.jwtredis.model.dto.AttachFileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@TableName(value = "attach_file")
public class AttachFile {
    @TableId(value = "id", type = IdType.AUTO) // AUTO_INCREMENT 사용
    private Long id;
    private String filePath;
    private String fileKey;
    private String fileName;
    private String fileDir;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AttachFileDto.Response toDto() {
        return AttachFileDto.Response.builder()
                .id(this.id)
                .filePath(this.filePath)
                .fileKey(this.fileKey)
                .fileDir(this.fileDir)
                .fileName(this.fileName)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public void changeFile(MultipartFile file, String filePath) {
        this.fileName = file.getOriginalFilename();
        this.filePath = filePath;
    }
}
