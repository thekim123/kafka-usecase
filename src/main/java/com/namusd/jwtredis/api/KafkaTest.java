package com.namusd.jwtredis.api;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaTest {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/kafka/produce")
    public void sendMessage(String message) {
        kafkaTemplate.send("video-processing-request", message);
    }

    @KafkaListener(topics = "video-processing-request", groupId = "video-processing-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
