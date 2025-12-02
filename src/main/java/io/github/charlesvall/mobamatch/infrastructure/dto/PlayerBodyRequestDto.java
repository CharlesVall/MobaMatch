package io.github.charlesvall.mobamatch.infrastructure.dto;

import io.github.charlesvall.mobamatch.domain.model.Region;
import io.github.charlesvall.mobamatch.domain.model.Role;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlayerBodyRequestDto(
        @NotBlank(message = "Username must not be empty")
        String username,

        @Min(value = 1, message = "Skill level must be at least 1")
        @Max(value = 100, message = "Skill level cannot exceed 100")
        int skillLevel,

        @NotNull Region region,
        @NotNull Role preferredRole
) {}