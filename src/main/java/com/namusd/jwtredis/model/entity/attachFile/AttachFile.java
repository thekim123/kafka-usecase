package com.namusd.jwtredis.model.entity.attachFile;


import com.namusd.jwtredis.model.dto.AttachFileDto;
import com.namusd.jwtredis.model.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Builder
@Table(name = "attach_file")
@NoArgsConstructor
@Entity
public class AttachFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;

    //TODO: 뺼까? 쓸모없어보이는데....
    private String fileKey;
    private String fileName;
    private String fileDir;

    @Enumerated(EnumType.STRING)
    @Column(name="file_type")
    private AttachFileType fileType;

    public AttachFileDto.Response toDto() {
        return AttachFileDto.Response.builder()
                .id(this.id)
                .filePath(this.filePath)
                .fileKey(this.fileKey)
                .fileDir(this.fileDir)
                .fileName(this.fileName)
                .build();
    }

    public void changeFile(MultipartFile file, String filePath) {
        this.fileName = file.getOriginalFilename();
        this.filePath = filePath;
    }
}
