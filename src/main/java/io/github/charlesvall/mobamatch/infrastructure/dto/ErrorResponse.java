package io.github.charlesvall.mobamatch.infrastructure.dto;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        String type,
        String title,
        int status,
        String details,
        String instance
) {}
