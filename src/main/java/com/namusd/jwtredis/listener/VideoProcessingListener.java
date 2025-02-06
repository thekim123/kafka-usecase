package com.namusd.jwtredis.listener;

import com.namusd.jwtredis.model.dto.video.TimelineDto;
import com.namusd.jwtredis.service.VideoService;
import com.namusd.jwtredis.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class VideoProcessingListener {

    private final VideoService videoService;

    @KafkaListener(topics = "video-timeline-response", groupId = "video-timeline-group")
    public void handleTimelineResponse(ConsumerRecord<String, String> record) {
        TimelineDto.KafkaResponseMessage response = ParseUtil.fromJson(record.value(), TimelineDto.KafkaResponseMessage.class);
        videoService.saveTimelineFrameData(response);
    }
}
