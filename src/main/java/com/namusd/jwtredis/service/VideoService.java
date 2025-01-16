package com.namusd.jwtredis.service;

import com.namusd.jwtredis.callback.LogCallback;
import com.namusd.jwtredis.config.auth.PrincipalDetails;
import com.namusd.jwtredis.model.domain.PageRequest;
import com.namusd.jwtredis.model.domain.PageResult;
import com.namusd.jwtredis.model.dto.ConvertDto;
import com.namusd.jwtredis.model.dto.VideoDto;
import com.namusd.jwtredis.model.entity.User;
import com.namusd.jwtredis.model.entity.video.OriginalFrame;
import com.namusd.jwtredis.model.entity.video.Video;
import com.namusd.jwtredis.model.vo.VideoRegisterVo;
import com.namusd.jwtredis.persistence.repository.OriginalFrameRepository;
import com.namusd.jwtredis.persistence.repository.VideoRepository;
import com.namusd.jwtredis.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final VideoRepository videoRepository;
    private final OriginalFrameRepository originalFrameRepository;

    @Transactional
    public String insertVideo(Authentication auth, VideoRegisterVo vo) {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();

        Video video = Video.builder()
                .videoId(vo.getVideoId())
                .videoTitle(vo.getVideoFileName())
                .videoFileId(vo.getAttachFileId())
                .ownerId(loginUser.getId())
                .build();
        Video entity = videoRepository.save(video);
        return entity.getVideoId();
    }

    public void assembleFrame(String fileDir) {
        ProducerRecord<String, String> record = new ProducerRecord<>("video-assemble-request", "frameDir", ParseUtil.toJson(fileDir));
        log.info("partition: {}", record.partition());
        var future = kafkaTemplate.send(record);
        future.addCallback(new LogCallback());
    }

    @Transactional
    public void saveOriginalFrameData(ConvertDto.Response response) {
        OriginalFrame originalFrame = OriginalFrame.builder()
                .startSequence(response.getStartSequence())
                .endSequence(response.getEndSequence())
                .videoId(response.getRequestId())
                .build();
        originalFrameRepository.save(originalFrame);
    }

    @Transactional(readOnly = true)
    public PageResult<VideoDto.Response> getVideoList(Authentication auth, PageRequest pageRequest) {
        User loginUser = ((PrincipalDetails) auth.getPrincipal()).getUser();

        // MyBatis 매퍼 호출
        List<Video> videos = videoRepository.findVideoPage(pageRequest, loginUser);
        List<VideoDto.Response> dtoList = videos.stream()
                .map(Video::toDto)
                .toList();

        // 총 데이터 개수 조회
        int totalCount = videoRepository.countAllVideos(loginUser);

        // Spring Data의 Page 객체로 변환
        return new PageResult<>(dtoList, pageRequest, totalCount);
    }
}
