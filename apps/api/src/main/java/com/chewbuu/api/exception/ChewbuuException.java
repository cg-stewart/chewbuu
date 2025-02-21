package com.chewbuu.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ChewbuuException extends RuntimeException {
    private final HttpStatus status;

    public ChewbuuException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
