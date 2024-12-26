package com.namusd.jwtredis.handler.ex;

import java.io.Serial;

public class BadRequestApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestApiException(String message) {
        super(message);
    }
}
