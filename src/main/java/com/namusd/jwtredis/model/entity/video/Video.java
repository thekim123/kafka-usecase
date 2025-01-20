package com.namusd.jwtredis.model.entity.video;

import com.namusd.jwtredis.model.dto.VideoDto;
import com.namusd.jwtredis.model.entity.AttachFile;
import com.namusd.jwtredis.model.entity.User;
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
public class Video {
    @Id
    @GeneratedValue(generator = "video_uuid")
    @GenericGenerator(name = "video_uuid", strategy = "uuid2")
    @Column(name = "video_id", columnDefinition = "BINARY(16)")
    private UUID videoId;

    @Column(name = "video_title", nullable = false, length = 100)
    private String videoTitle;

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
                .build();
    }
}
