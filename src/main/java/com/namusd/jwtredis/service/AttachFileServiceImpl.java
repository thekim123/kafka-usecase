package com.namusd.jwtredis.service;

import com.namusd.jwtredis.handler.ex.EntityNotFoundException;
import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
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

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachFileServiceImpl implements AttachFileService {

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
        UUID uuid = UUID.randomUUID();
        String fileKey = uuid.toString();
        String filePath = FileUtil.buildFilePath(dir, file.getOriginalFilename());
        AttachFile attachFile = AttachFile.builder()
                .fileName(file.getOriginalFilename())
                .fileDir(dir)
                .filePath(String.format("%s/%s/%s", this.endpoint, this.bucket, filePath))
                .build();
        return fileRepository.save(attachFile);
    }


    @Override
    public void uploadFile(MultipartFile file, String dir) {
        String objectName = dir + file.getOriginalFilename();
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
