package io.github.charlesvall.mobamatch.infrastructure.mapper.match;

import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.infrastructure.dto.MatchDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchDtoMapper {
    Match toDomain(MatchDto matchDto);
    MatchDto toResponseDto(Match match);
}
