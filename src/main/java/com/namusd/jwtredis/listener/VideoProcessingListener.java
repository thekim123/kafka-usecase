package com.namusd.jwtredis.listener;

import com.namusd.jwtredis.facade.VideoFacade;
import com.namusd.jwtredis.model.dto.ConvertDto;
import com.namusd.jwtredis.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class VideoProcessingListener {

    private final VideoFacade videoFacade;

    @KafkaListener(topics = "video-processing-response", groupId = "video-processing-gruop")
    public void handleResponse(ConsumerRecord<String, String> record) {
        ConvertDto.Response response = ParseUtil.fromJson(record.value(), ConvertDto.Response.class);
        videoFacade.saveFrameMetadata(response);
    }
}
