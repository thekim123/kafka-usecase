package com.namusd.jwtredis.model.dto;

import com.namusd.jwtredis.model.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

public class BoardDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class PostRequest {
        private String title;
        private String content;
        private Long authorId;

        public Board toEntity() {
            return Board.builder()
                    .title(this.title)
                    .content(this.content)
                    .authorId(this.authorId)
                    .build();
        }
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private UserDto.Response author;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;

        public void withAuthor(UserDto.Response author) {
            this.author = author;
        }
    }
}
