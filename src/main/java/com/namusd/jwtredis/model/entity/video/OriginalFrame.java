package com.namusd.jwtredis.model.entity.video;

import com.namusd.jwtredis.model.dto.video.OriginalFrameDto;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "original_frame")
@Entity
// MinIO 디렉토리는 videoId가 디렉토리가 됨.
public class OriginalFrame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    private int startSequence;
    private int endSequence;

    public OriginalFrameDto.Response toDto() {
        return OriginalFrameDto.Response.builder()
                .endSequence(this.endSequence)
                .startSequence(this.startSequence)
                .build();
    }
}
