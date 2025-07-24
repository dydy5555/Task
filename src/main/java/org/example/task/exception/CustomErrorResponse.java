package org.example.task.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}