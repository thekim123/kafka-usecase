package com.namusd.jwtredis.api;

import com.namusd.jwtredis.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/api/attach")
@RequiredArgsConstructor
@Slf4j
public class AttachFileController {

    private final AttachFileService attachFileService;

    /**
     * 이것으로 파일을 업로드하지 말 것.
     * 테스트용으로만 사용할것.
     * 운영에서는 반드시 제거
     * @param dir
     * @param file
     * @return
     */
    @PostMapping("/{dir}")
    public ResponseEntity<?> uploadFile(@PathVariable String dir, @RequestParam("file") MultipartFile file) {
        attachFileService.uploadFile(file);
        return ResponseEntity.created(URI.create("asdf")).build();
    }

}
