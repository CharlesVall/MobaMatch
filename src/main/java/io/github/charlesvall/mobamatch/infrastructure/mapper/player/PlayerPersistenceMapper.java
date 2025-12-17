package io.github.charlesvall.mobamatch.infrastructure.mapper.player;

import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.infrastructure.entity.PlayerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerPersistenceMapper {
    PlayerEntity toEntity(Player player);
    Player fromEntity(PlayerEntity entity);
}
