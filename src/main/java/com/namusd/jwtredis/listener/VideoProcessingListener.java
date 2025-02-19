package com.namusd.jwtredis.listener;

import com.namusd.jwtredis.facade.VideoFacade;
import com.namusd.jwtredis.model.dto.video.MessageDto;
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
    private final VideoFacade videoFacade;

    @KafkaListener(topics = "video-timeline-response", groupId = "video-timeline-group")
    public void handleTimelineResponse(ConsumerRecord<String, String> record) {
        TimelineDto.KafkaResponseMessage response = ParseUtil.fromJson(record.value(), TimelineDto.KafkaResponseMessage.class);
    }

    @KafkaListener(topics = "video-processing-responses", groupId = "video-processing-group")
    public void handleVideoProcessingResponse(ConsumerRecord<String, String> record) {
        MessageDto.KafkaProcessedResponseMessage response = ParseUtil.fromJson(record.value(), MessageDto.KafkaProcessedResponseMessage.class);
        videoFacade.updateMetadataFromProcessedResponse(response);
    }

    @KafkaListener(topics = "video-finalize-responses", groupId = "video-finalize-group")
    public void handleVideoFinalizeResponse(ConsumerRecord<String, String> record) {
        MessageDto.KafkaFinalizedResponseMessage response = ParseUtil.fromJson(record.value(), MessageDto.KafkaFinalizedResponseMessage.class);
        videoFacade.updateMetadataFromFinalizedResponse(response);
    }



    }
