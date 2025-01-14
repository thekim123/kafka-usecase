package com.namusd.jwtredis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namusd.jwtredis.model.dto.ConvertDto;
import io.minio.MinioClient;
import com.namusd.jwtredis.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConvertServiceImpl implements ConvertService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Map<String, CompletableFuture<ConvertDto.Response>> responseFutures = new ConcurrentHashMap<>();  // 요청 ID와 CompletableFuture를 매핑하여 응답을 처리

    @Value("${spring.minio.bucket}")
    private String bucket;

    @Override
    public ConvertDto.Response sendUrlAndGetProcessedUrl(ConvertDto.Request request) {
        String requestId = UUID.randomUUID().toString();    // 고유 요청 ID 생성
        ConvertDto.Request updatedRequest = ConvertDto.Request.createWithRequestId(request.getUrl(), requestId, bucket);

        // CompletableFuture로 비동기 응답 처리
        CompletableFuture<ConvertDto.Response> future = new CompletableFuture<>();
        responseFutures.put(requestId, future);


        // 요청 메시지 전송
        try {
            kafkaTemplate.send("video-processing-requests", requestId, ParseUtil.toJson(updatedRequest));
            log.info("$$$$$$ Sent video processing request: {}", updatedRequest);
        } catch (Exception e) {
            log.error("$$$$$$ Error sending Kafka message", e);
            throw new RuntimeException("$$$$$$ Error sending Kafka message", e);
        }

        try {
            // 응답 대기 (90초 타임아웃)
            return future.get(90, TimeUnit.SECONDS);
        }catch (TimeoutException e) {
            throw new RuntimeException("Response timed out");
        } catch (Exception e) {
            throw new RuntimeException("Failed to process video request", e);
        } finally {
            responseFutures.remove(requestId);  // 응답 후 맵에서 제거
        }

    }


    @KafkaListener(topics = "video-processing-response", groupId = "video-processing-gruop")
    public void handleResponse(String message) {
        ConvertDto.Response response = ParseUtil.fromJson(message, ConvertDto.Response.class);
        CompletableFuture<ConvertDto.Response> future = responseFutures.get(response.getRequestId());

        if (future != null) {
            future.complete(response);  // 응답 전달
            log.info("$$$$$$ Received video processing response: {}", response);
        } else {
            log.warn("$$$$$$ No matching request for response: {}", response);
        }
    }


    // Helper 메서드: 객체를 JSON 문자열로 변환
    private String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    // Helper 메서드: JSON 문자열을 객체로 변환
    private <T> T fromJson(String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }

}
