package com.namusd.jwtredis.model.entity.video;

import com.namusd.jwtredis.model.dto.video.MessageDto;
import com.namusd.jwtredis.model.dto.video.TimelineDto;
import com.namusd.jwtredis.model.dto.video.VideoDto;
import com.namusd.jwtredis.model.entity.BaseTimeEntity;
import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import com.namusd.jwtredis.model.entity.user.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
@Builder
@Table(name = "video")
@Entity
@NoArgsConstructor
public class Video extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "video_uuid")
    @GenericGenerator(name = "video_uuid", strategy = "uuid2")
    @Column(name = "video_id", columnDefinition = "BINARY(16)")
    private UUID videoId;   // 각 영상 프로젝트를 구분하는 UUID

    @Column(name = "work_title", nullable = false)
    private String workTitle;   // 작업 이름 (예: 금발머리 여성)

    @Column(name = "video_title", nullable = false, length = 100)
    private String videoTitle;  // 원본 파일명

    @Column(name = "duration", columnDefinition = "DOUBLE")
    private double duration;    // 영상 길이 (초)

    @Column(name = "fps")
    private Integer fps;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "total_frame_count")
    private Integer totalFrameCount;    // 총 프레임 수

    /**
     * @apiNote 비디오 작업의 상태를 나타내는 상태값
     * @see VideoStatus
     */
    @Column(name = "video_status", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VideoStatus videoStatus = VideoStatus.REGISTERED;

    @OneToOne
    private AttachFile videoFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    public VideoDto.Response toDto() {
        return VideoDto.Response.builder()
                .videoId(this.videoId.toString())
                .videoTitle(this.videoTitle)
                .videoFileId(this.videoFile.getId())
                .ownerId(this.owner.getId())
                .workTitle(this.workTitle)
                .videoStatus(this.videoStatus)
                .build();
    }

    public VideoDto.Detail toDetail() {
        return VideoDto.Detail.builder()
                .videoId(this.videoId.toString())
                .videoTitle(this.videoTitle)
                .workTitle(this.workTitle)
                .duration(this.duration)
                .owner(this.owner.toDto())
                .videoInfo(this.videoFile.toDto())
                .fps(this.fps)
                .totalFrameCount(this.totalFrameCount)
                .build();
    }

    public void withVideoFile(AttachFile attachFile) {
        this.videoFile = attachFile;
    }

    public void registerMetadata(TimelineDto.KafkaResponseMessage response) {
        this.videoStatus = VideoStatus.READY;
        this.fps = response.getFps();
        this.totalFrameCount = response.getTotalFrameCount();
    }

    //TODO: registerMetadata, updateMetadata 통합
    public void updateMetadata(MessageDto.KafkaProcessedResponseMessage response) {
        this.videoStatus = VideoStatus.READY;
        this.fps = response.getVideoMetadata().getFps();
        this.width = response.getVideoMetadata().getWidth();
        this.height = response.getVideoMetadata().getHeight();
        this.totalFrameCount = response.getVideoMetadata().getTotalFrames();
        this.duration = response.getVideoMetadata().getDuration();
    }


    public void updateMetadata() {
        this.videoStatus = VideoStatus.COMPLETE;
    }




}
