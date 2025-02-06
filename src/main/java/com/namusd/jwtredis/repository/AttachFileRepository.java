package com.namusd.jwtredis.repository;

import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {
}
