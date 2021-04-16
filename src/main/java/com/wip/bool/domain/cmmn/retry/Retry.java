package com.wip.bool.domain.cmmn.retry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Retry {

    public void sleep(long millisecond) {

        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            log.error("sleep 실패");
        }
    }
}
