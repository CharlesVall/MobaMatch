package io.github.charlesvall.mobamatch.domain.model;

public class PlayerValidate {

    private static final int MIN_LEVEL = 0;
    private static final int MAX_LEVEL = 100;

    public static void validateUsername(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be blank");
        }

        if (value.length() < 3) {
            throw new IllegalArgumentException("Player name must be at least 3 characters");
        }

        if (value.length() > 50) {
            throw new IllegalArgumentException("Player name cannot exceed 50 characters");
        }

        if (!value.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException("Player name can only contain alphanumeric characters, underscore and dash");
        }
    }

    public static void validateSkillLevel(int value) {
        if (value < MIN_LEVEL || value > MAX_LEVEL) {
            throw new IllegalArgumentException(
                    String.format("Skill level must be between %d and %d, got: %d",
                            MIN_LEVEL, MAX_LEVEL, value)
            );
        }
    }
}
