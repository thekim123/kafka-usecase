package com.namusd.jwtredis.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public class LogCallback implements ListenableFutureCallback<Object> {
    @Override
    public void onFailure(Throwable ex) {
        log.warn("$$$$$$ No matching request for response: {}", ex.getMessage());
    }

    @Override
    public void onSuccess(Object result) {
        log.info("$$$$$$ Received response: {}", result);
    }
}
