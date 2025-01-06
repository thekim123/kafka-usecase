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


    /**
     * 파일을 '수정'함에도 Post를 쓰는 이유는
     * HTTP PUT은 일반적으로 application/json과 같은 구조화된 데이터 전송에 사용되며,
     * 파일 업로드는 POST가 더 적합하기 때문.
     * @param request
     * @return
     */
    @PostMapping(value = "/frame", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateFrame(@ModelAttribute FrameUpdateDto.PutRequest request) {
        AttachFileDto.Response dto = fileService.updateFile(request.getFile(), request.getId());
        assembleService.assembleFrame(dto);
        return ResponseEntity.ok().build();
    }

    @KafkaListener(topics = "video-assemble-request", groupId = "video-assemble-response")
    public ResponseEntity<?> completeFrame(ConsumerRecord<String, String> record) {
        log.info(record.toString());
        return ResponseEntity.ok().build();
    }

}
