package com.namusd.jwtredis.service;

import com.namusd.jwtredis.callback.LogCallback;
import com.namusd.jwtredis.config.auth.PrincipalDetails;
import com.namusd.jwtredis.handler.ex.EntityNotFoundException;
import com.namusd.jwtredis.model.dto.video.MessageDto;
import com.namusd.jwtredis.model.dto.video.TimelineDto;
import com.namusd.jwtredis.model.dto.video.VideoDto;
import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import com.namusd.jwtredis.model.entity.user.User;
import com.namusd.jwtredis.model.entity.video.Video;
import com.namusd.jwtredis.repository.VideoRepository;
import com.namusd.jwtredis.service.helper.VideoServiceHelper;
import com.namusd.jwtredis.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final VideoRepository videoRepository;

    @Transactional
    public String insertVideo(Authentication auth, String workTitle, MultipartFile file) {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();
//        double duration = VideoUtil.getVideoDuration(file); // python에서 처리 후 응답 데이터에서 duration 가져오도록 수정
        Video video = Video.builder()
                .videoTitle(file.getOriginalFilename())
                .workTitle(workTitle)
                .owner(loginUser)
//                .duration(duration)
                .build();
        Video entity = videoRepository.save(video);
        return entity.getVideoId().toString();
    }

    @Transactional
    public void withVideoFile(AttachFile attachFile, String videoId) {
        Video video = VideoServiceHelper.findVideoById(videoId, videoRepository);
        video.withVideoFile(attachFile);
    }

    public void assembleFrame(String fileDir) {
        ProducerRecord<String, String> record = new ProducerRecord<>("video-assemble-request", "frameDir", ParseUtil.toJson(fileDir));
        log.info("partition: {}", record.partition());
        var future = kafkaTemplate.send(record);
        future.addCallback(new LogCallback());
    }

    @Transactional
    public void saveTimelineFrameData(TimelineDto.KafkaResponseMessage response) {
        System.out.println("message response: " + response);
        Video video = VideoServiceHelper.findVideoById(response.getVideoId(), videoRepository);
        video.registerMetadata(response);
    }

    @Transactional(readOnly = true)
    public Page<VideoDto.Response> getVideoList(Authentication auth, Pageable pageable) {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();
        Page<Video> videos = videoRepository.findByOwnerOrderByCreatedAtDesc(loginUser, pageable);
        return videos.map(Video::toDto);
    }

    @Transactional(readOnly = true)
    public VideoDto.Detail getVideoDetail(Authentication auth, String videoId) {
        Video video = videoRepository.findVideoDetail(UUID.fromString(videoId))
                .orElseThrow(() -> new EntityNotFoundException("없어"));
        return video.toDetail();
    }

    /**
     * original to proccesd 처리 후 완료 응답에서 영상의 메타데이터 추출해
     * video의 메타데이터를 업데이트 (READY to
     * @param response
     */
    @Transactional
    public void saveProccesdMetadata(MessageDto.KafkaProcessedResponseMessage response) {
        log.info("complete proccesed message response: " + response);
        Video video = VideoServiceHelper.findVideoById(response.getVideoId(), videoRepository);
        video.updateMetadata(response);
    }

    /**
     * processed to final 처리 후 완료 응답에서 영상의 메타데이터 추출해
     * video의 메타데이터를 업데이트 (COMPLETE)
     * @param response
     */
    @Transactional
    public void saveFinalizedMetadata(MessageDto.KafkaFinalizedResponseMessage response) {
        log.info("complete finalized message response: " + response);
        Video video = VideoServiceHelper.findVideoById(response.getVideoId(), videoRepository);
        video.updateMetadata();
    }
}
