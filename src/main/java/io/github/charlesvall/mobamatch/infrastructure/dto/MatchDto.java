package io.github.charlesvall.mobamatch.infrastructure.dto;

import io.github.charlesvall.mobamatch.domain.model.Region;

import java.util.List;

public record MatchDto (
        String id,
        List<String> playerIds,
        int averageSkill,
        Region region
){}
