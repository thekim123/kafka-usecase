package com.namusd.jwtredis.api;

import com.namusd.jwtredis.model.dto.video.ConvertDto;
import com.namusd.jwtredis.model.dto.video.FrameDto;
import com.namusd.jwtredis.service.AttachFileService;
import com.namusd.jwtredis.service.ConvertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/frame")
@RequiredArgsConstructor
@Slf4j
public class FrameProcessingController {

    private final ConvertService convertService;
    private final AttachFileService fileService;

    @PostMapping("/di")
    public ResponseEntity<ConvertDto.Response> convertVideo(@RequestBody ConvertDto.Request request) {
        ConvertDto.Response response = convertService.sendUrlAndGetProcessedUrl(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping(value = "/update", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateFrame(@ModelAttribute FrameDto.PutRequest request) {
        fileService.updateFile(request.getFile(), request.getId());
        return ResponseEntity.ok().build();
    }

}
