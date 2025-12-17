package io.github.charlesvall.mobamatch.domain.exception;

public class MatchIsNotValidException extends RuntimeException {
    public MatchIsNotValidException() {
        super("Match invalid based on rules");
    }
}
