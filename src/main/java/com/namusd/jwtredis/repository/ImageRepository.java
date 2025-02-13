package com.namusd.jwtredis.repository;

import com.namusd.jwtredis.model.entity.video.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findById(Long id);
}
