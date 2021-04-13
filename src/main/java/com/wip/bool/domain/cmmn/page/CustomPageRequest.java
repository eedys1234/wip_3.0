package com.wip.bool.domain.cmmn.page;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
public class CustomPageRequest {

    private final int DEFAULT_SIZE = 10;
    private final int MAX_SIZE = 50;

    private int size;
    private int page;
    private Sort.Direction direction;

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
        return PageRequest.of(this.page, this.size, this.direction);
    }
}
