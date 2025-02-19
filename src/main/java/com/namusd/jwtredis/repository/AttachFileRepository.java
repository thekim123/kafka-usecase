package com.namusd.jwtredis.repository;

import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    // TODO: file테이블에서 videoId 사용해 fileDir 조건 걸어주면 찾을 수가 없음 .., fileName 하드코딩된 상태
    @Query("SELECT a.filePath FROM AttachFile a WHERE a.fileDir = :fileDir AND a.fileName = 'processed.mp4'")
    Optional<String> findFilePathByFileDir(@Param("fileDir") String fileDir);

    Optional<AttachFile> findFirstByFileDirAndFileNameOrderByCreatedAtDesc(String fileDir, String fileName);
}
