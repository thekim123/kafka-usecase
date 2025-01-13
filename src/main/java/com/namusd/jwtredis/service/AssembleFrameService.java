package com.namusd.jwtredis.service;

import com.namusd.jwtredis.config.kafka.LogCallback;
import com.namusd.jwtredis.model.dto.AttachFileDto;
import com.namusd.jwtredis.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AssembleFrameService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void assembleFrame(String fileDir) {
        ProducerRecord<String, String> record = new ProducerRecord<>("video-assemble-request", "frameDir", ParseUtil.toJson(fileDir));
        log.info("partition: {}", record.partition());
        var future = kafkaTemplate.send(record);
        future.addCallback(new LogCallback());
    }

}
