package com.spyatmycode.myjobhuntai.dto.apiResponse;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ApiResponse<T>(
        String message,
        T data,
        HttpStatus status,
        boolean success,
        LocalDate timeStamp,
        String token
    ) {

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, T data, String message, String token) {
        return ResponseEntity.status(status).body(new ApiResponse<T>(message, data, status, true ,LocalDate.now(), token));
    };

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, T data, String message, String token) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<T>(message, data, status, false, LocalDate.now(), null));
    };

}
