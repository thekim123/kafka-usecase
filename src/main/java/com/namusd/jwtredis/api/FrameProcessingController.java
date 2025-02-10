package com.namusd.jwtredis.api;

import com.namusd.jwtredis.model.dto.video.FrameDto;
import com.namusd.jwtredis.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/frame")
@RequiredArgsConstructor
@Slf4j
public class FrameProcessingController {

    private final AttachFileService fileService;

    @PostMapping(value = "/update", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateFrame(@ModelAttribute FrameDto.PutRequest request) {
        fileService.updateFile(request.getFile(), request.getId());
        return ResponseEntity.ok().build();
    }

}
