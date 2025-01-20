package com.namusd.jwtredis.model.entity.video;

import com.namusd.jwtredis.model.entity.AttachFile;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "edited_frame")
@Entity
public class EditedFrame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edit_id")
    private VideoEdit edit;

    @OneToOne
    private AttachFile editFile;
}
