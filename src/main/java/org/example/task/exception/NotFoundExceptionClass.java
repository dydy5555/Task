package org.example.task.exception;

public class NotFoundExceptionClass extends RuntimeException {
    public NotFoundExceptionClass(String message) {
        super(message);
    }
}
