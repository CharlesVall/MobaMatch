package io.github.charlesvall.mobamatch.infrastructure.dto;

import io.github.charlesvall.mobamatch.domain.model.Region;
import io.github.charlesvall.mobamatch.domain.model.Role;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlayerResponseDto(
        @NotBlank String username,
        @Min(1) @Max(100) int skillLevel,
        @NotNull Region region,
        @NotNull Role preferredRole
) {}
