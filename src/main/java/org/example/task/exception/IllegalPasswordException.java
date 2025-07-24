package org.example.task.exception;

public class IllegalPasswordException extends RuntimeException {
    public IllegalPasswordException(String message) {
        super(message);
    }
}
