package com.namusd.jwtredis.repository;

import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.model.entity.video.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByVideoId(UUID videoId);

    Page<Video> findByOwnerOrderByCreatedAtDesc(User owner, Pageable pageable);
}
