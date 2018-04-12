package com.zhph.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class XSThreadPool extends ThreadPoolTaskExecutor {
    public XSThreadPool(){
        this.setMaxPoolSize(500);
        this.setCorePoolSize(5);
        this.setQueueCapacity(5000);
        this.setKeepAliveSeconds(10);
        this.initialize();
    }
}