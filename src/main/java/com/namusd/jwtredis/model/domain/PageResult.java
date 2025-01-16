package com.namusd.jwtredis.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class PageResult<T> {
    private List<T> content; // 실제 데이터 목록
    @Builder.Default
    private int page = 1;        // 현재 페이지
    @Builder.Default
    private int size = 20;        // 페이지 크기
    private int total;      // 총 데이터 개수
    private String sort;

    public PageResult(List<T> content, PageRequest pageRequest, int totalCount) {
        this.content = content;
        this.page = pageRequest.getPage();
        this.size = pageRequest.getSize();
        this.sort = pageRequest.getSort();
        this.total = totalCount;
    }

}
