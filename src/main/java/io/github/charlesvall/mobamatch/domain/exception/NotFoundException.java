package io.github.charlesvall.mobamatch.domain.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String id) {
        super("entity not found for id: " + id);
    }
}
