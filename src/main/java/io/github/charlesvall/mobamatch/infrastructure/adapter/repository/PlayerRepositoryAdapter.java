package io.github.charlesvall.mobamatch.infrastructure.adapter.repository;

import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.model.PlayerSearchCriteria;
import io.github.charlesvall.mobamatch.domain.port.out.PlayerRepository;
import io.github.charlesvall.mobamatch.infrastructure.adapter.repository.jpa.JpaPlayerRepository;
import io.github.charlesvall.mobamatch.infrastructure.adapter.repository.specification.PlayerSpecificationBuilder;
import io.github.charlesvall.mobamatch.infrastructure.entity.PlayerEntity;
import io.github.charlesvall.mobamatch.infrastructure.mapper.player.PlayerPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final JpaPlayerRepository jpaRepository;
    private final PlayerPersistenceMapper mapper;
    private final PlayerSpecificationBuilder specificationBuilder;


    @Override
    @Transactional
    public Player save(Player player) {
        PlayerEntity entity = mapper.toEntity(player);
        PlayerEntity saved = jpaRepository.save(entity);
        return mapper.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Player> findAll(Pageable pageable) {
        Page<PlayerEntity> entityPage = jpaRepository.findAll(pageable);
        return entityPage.map(mapper::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Player> findByCriteria(PlayerSearchCriteria criteria, Pageable pageable) {
        Specification<PlayerEntity> specification = specificationBuilder.build(criteria);
        return jpaRepository.findAll(specification, pageable)
                .map(mapper::fromEntity);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}

