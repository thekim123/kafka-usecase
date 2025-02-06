package com.namusd.jwtredis.repository;

import com.namusd.jwtredis.model.entity.user.User;
import com.namusd.jwtredis.model.entity.video.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Optional<Video> findByVideoId(UUID videoId);

    @Query("SELECT v FROM Video v " +
            "JOIN FETCH v.owner " +
            "JOIN FETCH v.videoFile " +
            "WHERE v.videoId = :videoId")
    Optional<Video> findVideoDetail(UUID videoId);


    Page<Video> findByOwnerOrderByCreatedAtDesc(User owner, Pageable pageable);
}
