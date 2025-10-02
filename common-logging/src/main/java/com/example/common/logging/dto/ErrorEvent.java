package com.example.common.logging.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ErrorEvent {
    private LocalDateTime timestamp;
    private String methodSignature;
    private String stacktrace;
    private String exceptionMessage;
    private Object inputParams; // можно массив/строку/Map
}
