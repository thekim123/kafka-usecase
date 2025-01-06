package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.dto.AttachFileDto;
import com.namusd.jwtredis.model.entity.AttachFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachFileService {

    void saveFileData(String dir, MultipartFile file);

    void uploadFile(MultipartFile file, String dir);

    void uploadFiles(List<MultipartFile> files, String dir);

    AttachFileDto.Response updateFile(MultipartFile file, Long fileId);

    void removeFile(Long fileId);
}
