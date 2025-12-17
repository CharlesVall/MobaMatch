package io.github.charlesvall.mobamatch.infrastructure.mapper.player;

import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.infrastructure.dto.PlayerRequestDto;
import io.github.charlesvall.mobamatch.infrastructure.dto.PlayerResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerDtoMapper {
    Player toDomain(PlayerRequestDto dto);
    PlayerResponseDto toResponseDto(Player player);
}
