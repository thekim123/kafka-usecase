package com.namusd.jwtredis.api;

import com.namusd.jwtredis.model.dto.AttachFileDto;
import com.namusd.jwtredis.model.dto.ConvertDto;
import com.namusd.jwtredis.model.dto.FrameUpdateDto;
import com.namusd.jwtredis.service.AssembleFrameService;
import com.namusd.jwtredis.service.AttachFileService;
import com.namusd.jwtredis.service.ConvertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingController {

    private final ConvertService convertService;
    private final AttachFileService fileService;
    private final AssembleFrameService assembleService;


    @PostMapping("/di")
    public ResponseEntity<ConvertDto.Response> convertVideo(@RequestBody ConvertDto.Request request) {
        ConvertDto.Response response = convertService.sendUrlAndGetProcessedUrl(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/frame/update", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateFrame(@ModelAttribute FrameUpdateDto.PutRequest request) {
        fileService.updateFile(request.getFile(), request.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/frame/assemble/{fileDir}")
    public ResponseEntity<?> assembleFrame(@PathVariable String fileDir) {
        assembleService.assembleFrame(fileDir);
        return ResponseEntity.ok().build();
    }

    @KafkaListener(topics = "video-assemble-request", groupId = "video-assemble-response")
    public ResponseEntity<?> completeFrame(ConsumerRecord<String, String> record) {
        log.info(record.toString());
        return ResponseEntity.ok().build();
    }

}
