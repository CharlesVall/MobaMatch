package io.github.charlesvall.mobamatch.infrastructure.mapper.match;

import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.infrastructure.dto.MatchResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchDtoMapper {
    Match toDomain(MatchResponseDto matchResponseDto);
    MatchResponseDto toResponseDto(Match match);
}
