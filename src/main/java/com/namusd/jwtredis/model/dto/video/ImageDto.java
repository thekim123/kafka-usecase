package com.namusd.jwtredis.model.dto.video;

import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class ImageDto {
    private Long id;
    private String filename;  // 파일명
    private String originalFilename;  // 원본 파일명
    private String imageType;
    private String videoId;
    private AttachFile imageFile;

    private MultipartFile imageFiles;

}
