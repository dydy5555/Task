package org.example.task.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.NonNull;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Hidden
public class GlobalException extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map <String,Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", Objects.requireNonNull(ex.getFieldError()).getDefaultMessage());
        return new ResponseEntity<>(body,status);
    }

    @ExceptionHandler(NotFoundExceptionClass.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(NotFoundExceptionClass exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalPasswordException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(IllegalPasswordException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.NOT_ACCEPTABLE.value(),
                "Cannot Proceed",
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(UserAlreadyExistsException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "already exist",
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(UnAuthorizeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(UnAuthorizeException exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Failed to authorize",
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    @ExceptionHandler(NullExceptionClass.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomErrorResponse> handleNullException(NullExceptionClass exception) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}