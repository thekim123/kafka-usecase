package com.namusd.jwtredis.api;

import com.namusd.jwtredis.facade.VideoFacade;
import com.namusd.jwtredis.model.dto.VideoDto;
import com.namusd.jwtredis.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingController {

    private final VideoFacade videoFacade;
    private final VideoService videoService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerVideo(Authentication auth, @RequestPart MultipartFile file) {
        String videoId = videoFacade.registerVideo(file, auth);
        return ResponseEntity.created(URI.create("/api/video/single/" + videoId)).build();
    }

    @GetMapping("/list")
    public ResponseEntity<?> getVideoList(Authentication auth) {
        Page<VideoDto.Response> videoList = videoService.getVideoList(auth);
        return ResponseEntity.ok(videoList);
    }

    @GetMapping("/single/{videoId}")
    public ResponseEntity<?> getSingleVideo(Authentication auth, @PathVariable("videoId") String videoId) {

        return ResponseEntity.ok(null);
    }

}
