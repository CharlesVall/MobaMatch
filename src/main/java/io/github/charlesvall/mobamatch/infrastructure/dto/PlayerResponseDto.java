package io.github.charlesvall.mobamatch.infrastructure.dto;

import io.github.charlesvall.mobamatch.domain.model.Region;
import io.github.charlesvall.mobamatch.domain.model.Role;

public record PlayerResponseDto(
        String id,
        String username,
        int skillLevel,
        Region region,
        Role preferredRole,
        boolean inMatch
) {}
