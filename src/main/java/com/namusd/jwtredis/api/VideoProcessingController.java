package com.namusd.jwtredis.api;

import com.namusd.jwtredis.facade.VideoFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingController {

    private final VideoFacade videoFacade;

    @PostMapping("/")
    public ResponseEntity<?> registerVideo(Authentication auth, @RequestPart MultipartFile file) {
        videoFacade.registerVideo(file, auth);
        return ResponseEntity.created(URI.create("registerVideo")).build();
    }

}
