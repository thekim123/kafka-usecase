package com.namusd.jwtredis.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class PageRequest {
    private int page;
    private int size;
    private String sort;
}
