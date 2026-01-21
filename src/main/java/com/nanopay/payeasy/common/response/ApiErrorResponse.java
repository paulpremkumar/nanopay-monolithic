package com.nanopay.payeasy.common.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ApiErrorResponse {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ApiErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // getters & setters
}

