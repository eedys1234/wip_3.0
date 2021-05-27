package com.wip.bool.cmmn;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse <T> {

    private String message;

    private int status;

    private T result;

    private ApiResponse(int status) {
        this(status, "");
    }

    private ApiResponse(int status, String message) {
        this(status, message, null);
    }

    private ApiResponse(int status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public static ApiResponse of(int status) {
        ApiResponse response = new ApiResponse(status);
        return response;
    }

    public static <T> ApiResponse of(int status, T result) {
        ApiResponse response = new ApiResponse(status, "", result);
        return response;
    }

    public static <T> ApiResponse of(int status, String message, T result) {
        ApiResponse response = new ApiResponse(status, message, result);
        return response;
    }
}
