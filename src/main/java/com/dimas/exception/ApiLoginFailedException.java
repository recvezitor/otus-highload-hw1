package com.dimas.exception;

public class ApiLoginFailedException extends RuntimeException {

    public ApiLoginFailedException() {
    }

    public ApiLoginFailedException(String message) {
        super(message);
    }

    public ApiLoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiLoginFailedException(Throwable cause) {
        super(cause);
    }

    public ApiLoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
