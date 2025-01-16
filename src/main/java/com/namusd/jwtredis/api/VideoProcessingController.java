package com.namusd.jwtredis.api;

import com.namusd.jwtredis.facade.VideoFacade;
import com.namusd.jwtredis.model.domain.PageRequest;
import com.namusd.jwtredis.model.domain.PageResult;
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
    public ResponseEntity<?> getVideoList(
            Authentication auth,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "video_id desc") String sort
    ) {
        PageRequest pageRequest = PageRequest.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .build();
        PageResult<VideoDto.Response> videoList = videoService.getVideoList(auth, pageRequest);
        return ResponseEntity.ok(videoList);
    }

    @GetMapping("/single/{videoId}")
    public ResponseEntity<?> getSingleVideo(Authentication auth, @PathVariable("videoId") String videoId) {

        return ResponseEntity.ok(null);
    }

}
