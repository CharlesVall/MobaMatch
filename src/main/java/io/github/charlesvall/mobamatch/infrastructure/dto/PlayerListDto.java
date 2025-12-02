package io.github.charlesvall.mobamatch.infrastructure.dto;

import java.util.List;

public record PlayerListDto(
        int length,
        List<PlayerResponseDto> players
) { }
