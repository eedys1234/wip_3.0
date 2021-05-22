package com.wip.bool.cmmn.retry;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class Retry {

    private int max;
    private int millisecond;

    public Retry() {
        this(5, 100);
    }

    public Retry(int max) {
        this(max, 100);
    }

    public Retry(int max, int millisecond) {
        this.max = max;
        this.millisecond = millisecond;
    }

    public void sleep(int millisecond) {

        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            log.error("sleep 실패");
        }
    }

    public <T> T perform(Supplier<T> supplier, loggable log, RuntimeException exceptions) {
        int count = 1;

        while(count++ <= max) {
            try {
                return supplier.get();
            }
            catch (Exception e) {
                log.start();
                sleep(count * millisecond);
            }
        }

        throw exceptions;
    }

    @FunctionalInterface
    private interface loggable {
        void start();
    }
}
