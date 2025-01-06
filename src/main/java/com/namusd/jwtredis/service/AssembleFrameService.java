package com.namusd.jwtredis.service;

import com.namusd.jwtredis.config.kafka.LogCallback;
import com.namusd.jwtredis.model.dto.AttachFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Service
@RequiredArgsConstructor
@Slf4j
public class AssembleFrameService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void assembleFrame(AttachFileDto.Response response) {
        ProducerRecord<String, String> record = new ProducerRecord<>("video-assemble-request", "frameDir", response.getFileDir());
        var future = kafkaTemplate.send(record);
        future.addCallback(new LogCallback());
    }

}
