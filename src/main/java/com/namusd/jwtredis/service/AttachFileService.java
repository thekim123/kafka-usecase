package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.entity.AttachFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachFileService {

    void uploadFile(MultipartFile file);

    void uploadFiles(List<MultipartFile> files);

    void updateFile(MultipartFile file, AttachFile attachFile);

    void removeFile(AttachFile attachFile);
}
