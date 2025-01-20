package com.namusd.jwtredis.facade;

import com.namusd.jwtredis.callback.LogCallback;
import com.namusd.jwtredis.model.constant.ConvertOperation;
import com.namusd.jwtredis.model.constant.FilePathConstant;
import com.namusd.jwtredis.model.dto.ConvertDto;
import com.namusd.jwtredis.model.entity.AttachFile;
import com.namusd.jwtredis.model.vo.VideoRegisterVo;
import com.namusd.jwtredis.service.AttachFileService;
import com.namusd.jwtredis.service.VideoService;
import com.namusd.jwtredis.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoFacade {
    private final AttachFileService attachFileService;
    private final VideoService videoService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.minio.bucket}")
    private String bucket;

    public String registerVideo(MultipartFile file, String workTitle,Authentication auth) {
        String videoId = videoService.insertVideo(auth, workTitle,file);

        AttachFile attachFile = attachFileService.saveFileData("video/" + videoId, file);
        attachFileService.uploadFile(file, FilePathConstant.VIDEO_UPLOAD_PATH + videoId);

        videoService.withVideoFile(attachFile, videoId);

        ConvertDto.Request request = ConvertDto.Request.builder()
                .bucket(this.bucket)
                .bucket_name(bucket)
                .operation(ConvertOperation.SPLIT.getValue())
                .url(attachFile.getFilePath())
                .requestId(videoId)
                .build();

        ProducerRecord<String, String> record
                = new ProducerRecord<>("video-processing-requests", request.getRequestId(), ParseUtil.toJson(request));
        var future = kafkaTemplate.send(record);
        future.addCallback(new LogCallback());
        return videoId;
    }


    public void saveFrameMetadata(ConvertDto.Response record) {
        videoService.saveOriginalFrameData(record);
    }
}
