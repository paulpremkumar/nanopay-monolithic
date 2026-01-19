package com.nanopay.payeasy.common;

import lombok.Data;

@Data
public class ApiResponse<Boolean,String,T> {
    private Boolean success;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(Boolean success, String message) {
        this(success, message, null);
    }

    // getters and setters
}

