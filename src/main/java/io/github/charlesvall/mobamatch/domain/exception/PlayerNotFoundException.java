package io.github.charlesvall.mobamatch.domain.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String id) {
        super("Player not found for id: " + id);
    }
}
