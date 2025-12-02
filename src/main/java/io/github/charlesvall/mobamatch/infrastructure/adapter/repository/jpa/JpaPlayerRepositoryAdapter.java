package io.github.charlesvall.mobamatch.infrastructure.adapter.repository.jpa;

import io.github.charlesvall.mobamatch.domain.exception.PlayerNotFoundException;
import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.port.out.PlayerRepository;
import io.github.charlesvall.mobamatch.infrastructure.adapter.repository.springdata.SpringDataPlayerRepository;
import io.github.charlesvall.mobamatch.infrastructure.entity.PlayerEntity;
import io.github.charlesvall.mobamatch.infrastructure.mapper.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaPlayerRepositoryAdapter implements PlayerRepository {

    private final SpringDataPlayerRepository jpaRepository;
    private final PlayerMapper playerMapper;

    @Override
    public Player save(Player player) {
        PlayerEntity entity = playerMapper.toEntity(player);
        PlayerEntity saved = jpaRepository.save(entity);
        return playerMapper.fromEntityToDomain(saved);
    }

    @Override
    public Optional<Player> findById(String id) {
        return jpaRepository.findById(id)
                .map(playerMapper::fromEntityToDomain);
    }

    @Override
    public List<Player> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(playerMapper::fromEntityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}

