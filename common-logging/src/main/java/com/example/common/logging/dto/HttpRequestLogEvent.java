package com.example.common.logging.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class HttpRequestLogEvent {
    private LocalDateTime timestamp;
    private String methodSignature;
    private String stacktrace;
    private String message;
    private List<Object> methodArgs;
}