package io.github.charlesvall.mobamatch.infrastructure.dto;

import java.util.List;

public record CreateMatchRequest(
        List<String> playerIds
) {}
