package com.namusd.jwtredis.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public class LogCallback implements ListenableFutureCallback<Object> {
    @Override
    public void onFailure(Throwable ex) {
        log.warn("fail to send request\nfail message: {}", ex.getMessage());
    }

    @Override
    public void onSuccess(Object result) {
        log.info("success to send request\nsend object: {}", result);
    }
}
