package com.namusd.jwtredis.model.entity;

import com.namusd.jwtredis.model.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Builder
@Table(name = "board")
@Entity
@NoArgsConstructor
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    public BoardDto.Response toDto() {
        return BoardDto.Response.builder()
                .id(this.id)
                .content(this.content)
                .title(this.title)
                .build();
    }

}
