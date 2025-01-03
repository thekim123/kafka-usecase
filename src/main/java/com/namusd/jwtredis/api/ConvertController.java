package com.namusd.jwtredis.api;

import com.namusd.jwtredis.model.dto.ConvertDto;
import com.namusd.jwtredis.service.ConvertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class ConvertController {

    private final ConvertService convertService;

//    @GetMapping("/di/{videoId}")
//    public ResponseEntity<ConvertDto.ConvertResponse> getVideoWithFrames(@PathVariable String videoId) {
//
//        return new ResponseEntity<>(ConvertDto.convertResponse, HttpStatus.OK);
//    }


    @PostMapping("/di")
    public ResponseEntity<ConvertDto.Response> convertVideo(@RequestBody ConvertDto.Request request) {
        ConvertDto.Response response = convertService.sendUrlAndGetProcessedUrl(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping("/di")
//    public ResponseEntity<Void> convertVideo(@RequestBody ConvertDto convertDto) {
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

}
