package com.namusd.jwtredis.repository;

import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    @Query("SELECT a.filePath FROM AttachFile a WHERE a.fileDir = :fileDir")
    Optional<String> findFilePathByFileDir(@Param("fileDir") String fileDir);

}
