package com.namusd.jwtredis.service;

import com.namusd.jwtredis.callback.LogCallback;
import com.namusd.jwtredis.config.auth.PrincipalDetails;
import com.namusd.jwtredis.model.dto.ConvertDto;
import com.namusd.jwtredis.model.dto.VideoDto;
import com.namusd.jwtredis.model.entity.AttachFile;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.model.entity.video.OriginalFrame;
import com.namusd.jwtredis.model.entity.video.Video;
import com.namusd.jwtredis.model.vo.VideoRegisterVo;
import com.namusd.jwtredis.repository.OriginalFrameRepository;
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


@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final VideoRepository videoRepository;
    private final OriginalFrameRepository originalFrameRepository;

    @Transactional
    public String insertVideo(Authentication auth, MultipartFile file) {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();
        Video video = Video.builder()
                .videoTitle(file.getOriginalFilename())
                .owner(loginUser)
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
    public void saveOriginalFrameData(ConvertDto.Response response) {
        Video video = VideoServiceHelper.findVideoById(response.getRequestId(), videoRepository);
        OriginalFrame originalFrame = OriginalFrame.builder()
                .startSequence(response.getStartSequence())
                .endSequence(response.getEndSequence())
                .video(video)
                .build();
        originalFrameRepository.save(originalFrame);
    }

    @Transactional(readOnly = true)
    public Page<VideoDto.Response> getVideoList(Authentication auth, Pageable pageable) {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();

        Page<Video> videos = videoRepository.findByOwner(loginUser, pageable);
        return videos.map(Video::toDto);
    }
}
