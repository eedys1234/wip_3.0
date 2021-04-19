package com.wip.bool.domain.cmmn.page;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@NoArgsConstructor
public class CustomPageRequest {

    private final int DEFAULT_SIZE = 10;
    private final int MAX_SIZE = 50;

    private int size;
    private int page;
    private Sort.Direction direction;

    @Builder
    public CustomPageRequest(int offset, int size) {
        int page = calculatePage(offset, size);
        this.page = page;
        this.size = size;

    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size ;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public PageRequest of() {
        return PageRequest.of(this.page, this.size);
    }

    public int calculatePage(int offset, int size) {
        return (offset / size) + 1;
    }
}
