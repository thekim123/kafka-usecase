package com.namusd.jwtredis.service.helper;

import com.namusd.jwtredis.handler.ex.EntityNotFoundException;
import com.namusd.jwtredis.model.entity.video.Video;
import com.namusd.jwtredis.repository.VideoRepository;

import java.util.UUID;

public class VideoServiceHelper {

    public static Video findVideoById(String videoId, VideoRepository repository) {
        return repository.findByVideoId(UUID.fromString(videoId)).orElseThrow(() -> new EntityNotFoundException("비디오 없음"));
    }
}
