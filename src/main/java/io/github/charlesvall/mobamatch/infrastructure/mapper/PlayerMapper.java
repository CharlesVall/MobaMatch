package io.github.charlesvall.mobamatch.infrastructure.mapper;

import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.infrastructure.dto.PlayerBodyRequestDto;
import io.github.charlesvall.mobamatch.infrastructure.dto.PlayerResponseDto;
import io.github.charlesvall.mobamatch.infrastructure.entity.PlayerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    Player toDomain(PlayerBodyRequestDto request);
    PlayerResponseDto toResponseDto(Player player);
    PlayerEntity toEntity(Player player);
    Player fromEntityToDomain(PlayerEntity playerEntity);
}
