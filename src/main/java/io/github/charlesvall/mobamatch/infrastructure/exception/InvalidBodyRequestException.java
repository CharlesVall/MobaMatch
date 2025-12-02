package io.github.charlesvall.mobamatch.infrastructure.exception;

public class InvalidBodyRequestException extends RuntimeException {
    public InvalidBodyRequestException() {
        super("Request body is invalid");
    }
}
