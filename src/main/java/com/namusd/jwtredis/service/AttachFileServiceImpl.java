package com.namusd.jwtredis.service;

import com.namusd.jwtredis.handler.ex.EntityNotFoundException;
import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import com.namusd.jwtredis.model.entity.attachFile.AttachFileType;
import com.namusd.jwtredis.repository.AttachFileRepository;
import com.namusd.jwtredis.util.FileUtil;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachFileServiceImpl implements AttachFileService {
// TODO: FileStorageService와 attachFileService로 분리?

    private final MinioClient minioClient;
    private final AttachFileRepository fileRepository;

    @Value("${spring.minio.bucket}")
    private String bucket;

    @Value("${spring.minio.endpoint}")
    private String endpoint;

    /**
     * 파일에 대한 정보를 DB에 저장.
     * 실제 파일은 MinIO에 저장
     *
     * @param dir  파일 저장 디렉토리
     * @param file MultipartFile
     */
    @Override
    @Transactional
    public AttachFile saveFileData(String dir, MultipartFile file) {
//        UUID uuid = UUID.randomUUID();
//        String fileKey = uuid.toString();
        String filePath = FileUtil.buildFilePath(dir, file.getOriginalFilename());
        AttachFile attachFile = AttachFile.builder()
                .fileName(file.getOriginalFilename())
                .fileDir(dir)
                .filePath(String.format("%s/%s/%s", this.endpoint, this.bucket, filePath))
                .build();
        return fileRepository.save(attachFile);
    }

    /**
     * 파일에 대한 정보를 새로운 이름으로 DB에 저장.
     *
     * @param newFileName 저장될 파일명
     */
    @Override
    @Transactional
    public AttachFile saveFileData(String dir, MultipartFile file, String newFileName, AttachFileType fileType) {
        String filePath = FileUtil.buildFilePath(dir, newFileName);
        AttachFile attachFile = AttachFile.builder()
                .fileName(newFileName)
                .fileDir(dir)
                .filePath(String.format("%s/%s/%s", this.endpoint, this.bucket, filePath))
                .fileType(fileType)
                .build();
        return fileRepository.save(attachFile);
    }

    /**
     * (원본 파일명이 없는 파일, 파일 유형과 함께)
     * 파일에 대한 정보를 DB에 저장 (ex: edtited_metadata.json)
     */
    @Override
    @Transactional
    public AttachFile saveFileData(String dir, String filename, AttachFileType fileType) {
        String filePath = FileUtil.buildFilePath(dir, filename);
        AttachFile attachFile = AttachFile.builder()
                .fileName(filename)
                .fileDir(dir)
                .filePath(String.format("%s/%s/%s", this.endpoint, this.bucket, filePath))
                .fileType(fileType)
                .build();
        return fileRepository.save(attachFile);
    }


    /**
     * MultipartFile을 MinIO에 업로드
     * @param file
     */
    @Override
    public void uploadFile(MultipartFile file, String dir) {
        String objectName = FileUtil.buildFilePath(dir, file.getOriginalFilename());
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(this.bucket)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    /**
     * MultiPartFile을 새로운 이름으로 MinIO에 업로드
     * @param file
     * @param newFileName 저장될 파일명
     */
    @Override
    public void uploadFile(MultipartFile file, String dir, String newFileName) {
        String objectName = FileUtil.buildFilePath(dir, newFileName);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(this.bucket)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    /**
     * jsonContent를 MinIO에 업로드
     * @param jsonContent
     */
    @Override
    public void uploadJsonContent(String jsonContent, String dir, String filename) {
        String objectName = FileUtil.buildFilePath(dir, filename);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonContent.getBytes(StandardCharsets.UTF_8));

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(this.bucket)
                            .object(objectName)
                            .stream(inputStream, jsonContent.length(), -1)
                            .contentType("application/json")
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error uploading JSON file: " + e.getMessage());
        }
    }


    /**
     * 여러 개의 MultipartFile을 MinIO에 업로드
     */
    @Override
    public void uploadFiles(List<MultipartFile> files, String dir) {
        files.forEach(file -> this.uploadFile(file, dir));
    }


    @Override
    public void updateFile(MultipartFile file, Long fileId) {
        AttachFile attachFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("파일 데이터가 DB에 없네요."));
        this.removeFile("/" + attachFile.getFileDir() + "/" + attachFile.getFileName());
        String filePath = FileUtil.buildFilePath(attachFile.getFileDir(), file.getOriginalFilename());
        filePath = String.format("%s/%s/%s", this.endpoint, this.bucket, filePath);
        attachFile.changeFile(file, filePath);
        this.uploadFile(file, attachFile.getFileDir());
        fileRepository.save(attachFile);
    }

    @Override
    public void removeFile(String filePath) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(this.bucket)
                            .object(filePath)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage());
        }

    }

    @Transactional
    public void deleteFileData(Long fileId) {
        AttachFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("파일 데이터가 DB에 없어요"));
        this.removeFile("/" + file.getFileDir() + "/" + file.getFileName());
        fileRepository.deleteById(fileId);
    }

    /**
     * @param filePath endpoint, bucket을 제외한 filepath
     * @return
     */
    @Override
    public InputStream getFile(String filePath) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(this.bucket)
                            .object(filePath)
                            .build()
            );
        } catch (MinioException e) {
            log.error(e.getMessage());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String getFilePath(String videoId) {
        return fileRepository.findFilePathByFileDir(videoId)
                .orElseThrow(() -> new EntityNotFoundException("파일 데이터가 DB에 없어요"));
    }
}
