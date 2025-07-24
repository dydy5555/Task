package org.example.task.exception;

public class UnAuthorizeException extends RuntimeException{
    public UnAuthorizeException(String message) {
    super(message);
}

}
