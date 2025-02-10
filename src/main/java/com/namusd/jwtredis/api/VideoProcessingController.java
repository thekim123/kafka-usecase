package com.namusd.jwtredis.api;

import com.namusd.jwtredis.facade.VideoFacade;
import com.namusd.jwtredis.model.dto.video.VideoDto;
import com.namusd.jwtredis.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> registerVideo(
            Authentication auth,
            @RequestPart("file") MultipartFile file,
            @RequestPart("workTitle") String workTitle
    ) {
        String videoId = videoFacade.registerVideo(file, workTitle, auth);
        return ResponseEntity.created(URI.create("/api/video/single/" + videoId)).build();
    }

    @GetMapping("/list")
    public ResponseEntity<?> getVideoList(
            Authentication auth,
            Pageable pageable
    ) {
        Page<VideoDto.Response> videoList = videoService.getVideoList(auth, pageable);
        return ResponseEntity.ok(videoList);
    }

    @GetMapping("/detail/{videoId}")
    public ResponseEntity<?> getVideoDetail(Authentication auth, @PathVariable("videoId") String videoId) {
        VideoDto.Detail detail = videoService.getVideoDetail(auth, videoId);
        return ResponseEntity.ok(detail);
    }


//    TODO: Targe_Image를 파라미터로 받도록 수정.. 여러장 받아 저장...
    @PostMapping("/edit/{videoId}")
    public ResponseEntity<?> getFinalVideo(Authentication auth, @PathVariable("videoId") String videoId, @RequestBody String editedMetadata) {
        videoFacade.getFinalVideo(videoId, editedMetadata);
        return ResponseEntity.ok().build();
    }
}
