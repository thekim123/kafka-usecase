package com.namusd.jwtredis.model.entity.video;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@Table(name = "video_edit")
@Entity
@NoArgsConstructor
public class VideoEdit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;
    private String editTitle;
}
