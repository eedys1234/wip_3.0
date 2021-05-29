package com.wip.bool.cmmn.logger;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

@Slf4j
public class CacheEventLogger implements CacheEventListener<Object, Object> {

    @Override
    public void onEvent(CacheEvent<? extends Object, ? extends Object> event) {
        log.info("cache event message key : {}, oldValue : {}, newValue : {}", event.getKey(), event.getOldValue(), event.getNewValue());
    }
}
