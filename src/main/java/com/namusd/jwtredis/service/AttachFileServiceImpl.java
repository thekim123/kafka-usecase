package com.namusd.jwtredis.service;

import com.namusd.jwtredis.model.entity.AttachFile;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachFileServiceImpl implements AttachFileService {

    private final MinioClient minioClient;

    @Value("${spring.minio.bucket}")
    private String bucket;

    @Override
    @Transactional
    public void uploadFile(MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(file.getOriginalFilename())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

           // return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    @Override
    public void uploadFiles(List<MultipartFile> files) {

    }

    @Override
    public void updateFile(MultipartFile file, AttachFile attachFile) {

    }

    @Override
    public void removeFile(AttachFile attachFile) {

    }
}
