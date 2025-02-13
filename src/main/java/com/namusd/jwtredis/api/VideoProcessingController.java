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
import java.util.List;

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
            @RequestPart(value = "images", required = false) List<MultipartFile> targetImages, // TODO: FRONT, SIDE FACE 구분 저장.. 현재 NORMAL TYPE 으로 한 번에 저장
            @RequestPart("workTitle") String workTitle
    ) {
        String videoId = videoFacade.registerVideo(file, targetImages, workTitle, auth);
        // TODO: return 진행중 보내고, 완료시 풀링 등으로 수정 예정
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


    @PostMapping("/finalize/{videoId}")
    public ResponseEntity<?> editAndFinalizeVideo(
            Authentication auth,
            @PathVariable("videoId") String videoId,
            @RequestBody String editedMetadata
    ) {
        videoFacade.editAndFinalizeVideo(auth, videoId, editedMetadata);
        return ResponseEntity.ok().build();
    }
}
