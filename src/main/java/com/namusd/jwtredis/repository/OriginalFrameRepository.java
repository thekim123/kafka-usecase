package com.namusd.jwtredis.repository;

import com.namusd.jwtredis.model.entity.video.OriginalFrame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginalFrameRepository extends JpaRepository<OriginalFrame, Long> {
}
