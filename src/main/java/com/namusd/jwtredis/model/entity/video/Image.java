package com.namusd.jwtredis.model.entity.video;

import com.namusd.jwtredis.model.entity.BaseTimeEntity;
import com.namusd.jwtredis.model.entity.attachFile.AttachFile;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@ToString
@Builder
@Table(name = "image")
@Entity
@NoArgsConstructor
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename", nullable = false, length = 100)
    private String filename;  // 파일명

     @Column(name = "original_filename", nullable = false, length = 100)
    private String originalFilename;  // 원본 파일명

    /**
     * @apiNote 사진의 유형
     */
    @Column(name = "video_status", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ImageType imageType = ImageType.NORMAL;

    @OneToOne
    private AttachFile imageFile;   //file_id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;



    public void withImageFile(AttachFile attachFile) {
        this.imageFile = attachFile;
    }



}
