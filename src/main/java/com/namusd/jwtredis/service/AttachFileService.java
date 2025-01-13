package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.dto.AttachFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachFileService {

    void saveFileData(String dir, MultipartFile file);

    void uploadFile(MultipartFile file, String dir);

    void uploadFiles(List<MultipartFile> files, String dir);

    void updateFile(MultipartFile file, Long fileId);

    void removeFile(String filePath);

    void deleteFileData(Long fileId);
}
