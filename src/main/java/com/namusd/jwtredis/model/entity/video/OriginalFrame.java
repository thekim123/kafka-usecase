package com.namusd.jwtredis.model.entity.video;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    private int startSequence;
    private int endSequence;
}
