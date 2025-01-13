package com.namusd.jwtredis.api;

import com.namusd.jwtredis.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/attach")
@RequiredArgsConstructor
@Slf4j
public class AttachFileController {

    private final AttachFileService attachFileService;

    /**
     * 파일업로드 샘플 코드
     * 이것을 마스터 파일 업로드 API로 사용하지 말 것.
     * 테스트 또는 개발용으로만 사용할것.
     * 운영에서는 반드시 제거
     */
    @PostMapping("/single/{dir}")
    public ResponseEntity<?> uploadFile(@PathVariable String dir, @RequestParam("file") MultipartFile file) {
        attachFileService.saveFileData(dir, file);
        attachFileService.uploadFile(file, dir);
        return ResponseEntity.created(URI.create("asdf")).build();
    }

    /**
     * 이것으로 파일을 업로드하지 말 것.
     * 테스트용으로만 사용할것.
     * 운영에서는 반드시 제거
     */
    @PostMapping("/list/{dir}")
    public ResponseEntity<?> uploadFiles(@PathVariable String dir, @RequestParam("file") List<MultipartFile> files) {
        attachFileService.uploadFiles(files, dir);
        return ResponseEntity.created(URI.create("asdf")).build();
    }

    @PutMapping
    public ResponseEntity<?> updateFile(@RequestParam("file") MultipartFile file, @RequestParam("fileId") Long fileId) {
        attachFileService.updateFile(file, fileId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId) {
        attachFileService.deleteFileData(fileId);
        return ResponseEntity.noContent().build();
    }

}
