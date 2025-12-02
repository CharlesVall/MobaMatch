package io.github.charlesvall.mobamatch.infrastructure.exception;

public class InvalidIdFormatException extends RuntimeException {
    public InvalidIdFormatException(String id) {
        super("provided id do not match the required format: " + id);
    }
}
