package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import com.namusd.jwtredis.model.entity.attachFile.AttachFileType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface AttachFileService {

    AttachFile saveFileData(String dir, MultipartFile file);

    @Transactional
    AttachFile saveFileData(String dir, String filename, AttachFileType fileType);

    AttachFile saveFileData(String dir, MultipartFile file, String newFileName, AttachFileType fileType);

    void uploadFile(MultipartFile file, String dir);

    void uploadJsonContent(String jsonContent, String filename, String dir);

    @Transactional
    void uploadFile(MultipartFile file, String dir, String newFileName);

    void uploadFiles(List<MultipartFile> files, String dir);

    void updateFile(MultipartFile file, Long fileId);

    void removeFile(String filePath);

    void deleteFileData(Long fileId);

    InputStream getFile(String filePath);

    String getFilePath(String videoId);
}
