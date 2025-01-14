package com.namusd.jwtredis.service;

import com.namusd.jwtredis.callback.LogCallback;
import com.namusd.jwtredis.model.entity.video.Video;
import com.namusd.jwtredis.model.vo.VideoRegisterVo;
import com.namusd.jwtredis.persistence.repository.VideoRepository;
import com.namusd.jwtredis.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final VideoRepository videoRepository;

    @Transactional
    public void insertVideo(Authentication auth, VideoRegisterVo vo) {
        Video video = Video.builder()
                .videoId(vo.getVideoId())
                .videoTitle(vo.getVideoFileName())
                .videoFileId(vo.getAttachFileId())
                .build();
        videoRepository.save(video);
    }

    public void assembleFrame(String fileDir) {
        ProducerRecord<String, String> record = new ProducerRecord<>("video-assemble-request", "frameDir", ParseUtil.toJson(fileDir));
        log.info("partition: {}", record.partition());
        var future = kafkaTemplate.send(record);
        future.addCallback(new LogCallback());
    }

}
