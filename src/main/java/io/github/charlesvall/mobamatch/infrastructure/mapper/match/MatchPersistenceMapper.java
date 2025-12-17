package io.github.charlesvall.mobamatch.infrastructure.mapper.match;

import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.infrastructure.entity.MatchEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchPersistenceMapper {
    MatchEntity toEntity(Match match);
    Match fromEntity(MatchEntity entity);
}
