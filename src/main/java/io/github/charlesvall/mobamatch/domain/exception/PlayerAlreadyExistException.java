package io.github.charlesvall.mobamatch.domain.exception;

public class PlayerAlreadyExistException extends RuntimeException {
    public PlayerAlreadyExistException(String username) {
        super("Player already exists: " + username);
    }
}
