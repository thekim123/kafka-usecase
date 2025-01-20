package com.namusd.jwtredis.socket;

import com.namusd.jwtredis.model.dto.FrameRequestDto;
import com.namusd.jwtredis.model.dto.FrameStreamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FrameSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("frames/request/{videoId}") // 클라이언트가 "/app/frames/request"로 요청
    public void streamFrames(@RequestBody FrameRequestDto request, @DestinationVariable String videoId) {
        log.info("video id: {}", videoId);
        int start = request.getStart();
        int end = request.getEnd();

        // 입력 값 검증
        if (start < 0 || end < start) {
            messagingTemplate.convertAndSend("/topic/frames/error", "Invalid range: start=" + start + ", end=" + end);
            return;
        }

        // 프레임 생성 및 전송
        InputStream inputStream;
        for (int i = start; i <= end; i++) {
            try {
                // 프레임 파일 경로를 지정
                String framePath = String.format("frames/frame_%04d.jpg", i);
                inputStream = getClass().getClassLoader().getResourceAsStream(framePath);
                if (inputStream == null) {
                    throw new FileNotFoundException("File not found: " + framePath);
                }

                // Base64 인코딩
                byte[] frameData = inputStream.readAllBytes();
                inputStream.close();
                String base64Frame = Base64.getEncoder().encodeToString(frameData);
                FrameStreamDto.Response dto = FrameStreamDto.Response.builder()
                        .direction(request.getDirection())
                        .base64Data(base64Frame)
                        .sequence(i)
                        .build();

                // 주제에 프레임 전송
                messagingTemplate.convertAndSend("/topic/frames/" + videoId, dto);
            } catch (Exception e) {
                messagingTemplate.convertAndSend("/topic/frames/error", "Error processing frame: " + i);
            }
        }
    }

}
