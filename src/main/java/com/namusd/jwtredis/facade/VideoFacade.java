package com.namusd.jwtredis.facade;

import com.namusd.jwtredis.callback.LogCallback;
import com.namusd.jwtredis.model.constant.FileNameConstant;
import com.namusd.jwtredis.model.constant.FilePathConstant;
import com.namusd.jwtredis.model.constant.ProcessStatusConstant;
import com.namusd.jwtredis.model.dto.video.ImageDto;
import com.namusd.jwtredis.model.dto.video.MessageDto;
import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import com.namusd.jwtredis.model.entity.attachFile.AttachFileType;
import com.namusd.jwtredis.model.entity.video.ImageType;
import com.namusd.jwtredis.model.entity.video.VideoStatus;
import com.namusd.jwtredis.service.AttachFileService;
import com.namusd.jwtredis.service.ImageService;
import com.namusd.jwtredis.service.VideoService;
import com.namusd.jwtredis.util.FileUtil;
import com.namusd.jwtredis.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoFacade {
    private final AttachFileService attachFileService;
    private final VideoService videoService;
    private final ImageService imageService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.minio.bucket}")
    private String bucket;

    /** 영상 저장 & 비식별 처리 요청 */
    public String registerVideo(MultipartFile file, List<MultipartFile> targetImages, String workTitle, Authentication auth) {
        // 영상 metadata DB 저장
        String videoId = videoService.insertVideo(auth, workTitle, file);
        // target 이미지 metadata DB 저장
        insertAndUploadTargetImage(targetImages, videoId);

        // 파일 확장자 추출하고 새로운 파일명 생성
        String newFileName = FileUtil.makeNewFileName(file.getOriginalFilename(), FileNameConstant.FILENAME_ORIGINAL);

        // file 메타데이터 DB 저장
        AttachFile attachFile = attachFileService.saveFileData(videoId, file, newFileName, AttachFileType.VIDEO);

        // bucket에 file 업로드
        attachFileService.uploadFile(file, videoId, newFileName);

        // 업로드한 file 정보 DB에 update
        videoService.withVideoFile(attachFile, videoId);

        MessageDto.KafkaRequestMessage request = MessageDto.KafkaRequestMessage.builder()
                .bucket(this.bucket)
                .bucket_name(this.bucket)
                .url(attachFile.getFilePath())
                .videoId(videoId)
                .build();


        ProducerRecord<String, String> timelineRecord
                = new ProducerRecord<>("video-timeline-requests", request.getVideoId(), ParseUtil.toJson(request));
        var timelineFuture = kafkaTemplate.send(timelineRecord);
        timelineFuture.addCallback(new LogCallback());

        ProducerRecord<String, String> processRecord
                = new ProducerRecord<>("video-processing-requests", request.getVideoId(), ParseUtil.toJson(request));
        var processFuture = kafkaTemplate.send(processRecord);
        processFuture.addCallback(new LogCallback());

        return videoId;
    }

    /** 비식별 처리에 사용될 target_image와 metadata를 Bucket과 DB에 저장*/
    @Transactional
    public void insertAndUploadTargetImage(List<MultipartFile> targetImages, String videoId) {
        if (targetImages == null || targetImages.isEmpty()) {
            return;
        }

        List<ImageDto> imageDtoList = new ArrayList<>();
        int count = 1;

        for (MultipartFile file : targetImages) {
            // 원본 파일명
            String originalFilename = file.getOriginalFilename();

            // 새로운 파일명 생성 (ex: target_image_1.png)
            String newFileName = FileUtil.makeNewFileName(originalFilename,
                    FileNameConstant.FILENAME_TARGET_IMAGE + count);
            count++;

            // 파일 메타데이터 저장
            AttachFile attachFile = attachFileService.saveFileData(videoId, file, newFileName, AttachFileType.IMAGE);

            // 파일 버킷 업로드
            attachFileService.uploadFile(file, videoId + FilePathConstant.TARGET_IMAGE_UPLOAD_PATH, newFileName);

            // DTO 생성
            ImageDto imageDto = ImageDto.builder()
                    .filename(newFileName)
                    .originalFilename(originalFilename)
                    .imageType(ImageType.NORMAL.name())  // 기본 이미지 유형 //TODO: NORMAL to FRONT, SIDE FACE 구분 저장..
                    .videoId(videoId)
                    .imageFile(attachFile) // 저장된 파일 ID 설정
                    .build();

            imageDtoList.add(imageDto);
        }

        // 이미지 metadata DB 저장
        imageService.insertImages(imageDtoList);
    }


    /** 최종 영상 처리 요청*/
    public void editAndFinalizeVideo(Authentication auth, String videoId, String editedMetadata) {
        // 파일이 저장된 경로
        String filePath = attachFileService.getFilePath(videoId);

        // edtied_metadata DB 저장
        attachFileService.saveFileData(videoId, FileUtil.withJsonExtension(FileNameConstant.FILENAME_EDITED_METADATA), AttachFileType.EDITED_METADATA);
        // edtied_metadata 버킷 업로드
        attachFileService.uploadJsonContent(editedMetadata, videoId, FileUtil.withJsonExtension(FileNameConstant.FILENAME_EDITED_METADATA));

        // video 처리 상태 변경
        videoService.changeVideoStatus(videoId, VideoStatus.CORRECTION);


        MessageDto.KafkaRequestMessage request = MessageDto.KafkaRequestMessage.builder()
                .bucket(this.bucket)
                .bucket_name(bucket)
                .url(filePath)
                .videoId(videoId)
                .build();

        ProducerRecord<String, String> record
                = new ProducerRecord<>("video-finalize-requests", request.getVideoId(), ParseUtil.toJson(request));
        var future = kafkaTemplate.send(record);
        future.addCallback(new LogCallback());
    }




    /**
     * origianl to processed 처리 완료 시
     * 영상 메타데이터 업데이트하고(width, height, fps, duration... ),
     * processed 영상의 메타데이터를 저장
     * @param response
     */
    public void updateMetadataFromProcessedResponse(MessageDto.KafkaProcessedResponseMessage response) {
        // original 영상 메타데이터 업데이트
        videoService.saveProcessedMetadata(response);

        // 영상 처리에 사용된 metadata.json 메타데이터를 db에 저장
        attachFileService.saveFileData(response.getVideoId(), FileUtil.withJsonExtension(FileNameConstant.FILENAME_METADATA), AttachFileType.METADATA);


        String fileName = FileNameConstant.FILENAME_PROCESSED + FileUtil.getFileExtension(response.getProcessedVideoUrl());
        // 처리된 proccesed 영상의 메타데이터를 attach_file 테이블에 저장
        attachFileService.saveFileData(response.getVideoId(), fileName, AttachFileType.VIDEO);
    }

    /**
     * processed to final 처리 완료 시
     * final 영상의 메타데이터를 저장
     * @param response
     */
    public void updateMetadataFromFinalizedResponse(MessageDto.KafkaFinalizedResponseMessage response) {
        if (!response.getStatus().equals(ProcessStatusConstant.PROCESS_STATUS_SUCCESS)){
            videoService.changeVideoStatus(response.getVideoId(), VideoStatus.ERROR);
            return;
        }

        // original 영상 메타데이터 업데이트
        videoService.saveFinalizedMetadata(response);

        String fileName = FileNameConstant.FILENAME_FINAL + FileUtil.getFileExtension(response.getFinalizedVideoUrl());
        // 처리된 proccesed 영상의 메타데이터를 attach_file 테이블에 저장
        // TODO: video 테이블에 매핑
        attachFileService.saveFileData(response.getVideoId(), fileName, AttachFileType.VIDEO);
    }



}
