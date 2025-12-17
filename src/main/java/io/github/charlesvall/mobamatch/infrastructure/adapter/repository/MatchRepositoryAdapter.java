package io.github.charlesvall.mobamatch.infrastructure.adapter.repository;

import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.MatchSearchCriteria;
import io.github.charlesvall.mobamatch.domain.port.out.MatchRepository;
import io.github.charlesvall.mobamatch.infrastructure.adapter.repository.jpa.JpaMatchRepository;
import io.github.charlesvall.mobamatch.infrastructure.adapter.repository.specification.MatchSpecificationBuilder;
import io.github.charlesvall.mobamatch.infrastructure.entity.MatchEntity;
import io.github.charlesvall.mobamatch.infrastructure.mapper.match.MatchPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryAdapter implements MatchRepository {

    private final JpaMatchRepository jpaRepository;
    private final MatchPersistenceMapper mapper;
    private final MatchSpecificationBuilder specificationBuilder;

    @Override
    public Match save(Match match) {
        MatchEntity entity = mapper.toEntity(match);
        MatchEntity saved = jpaRepository.save(entity);
        return mapper.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Match> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Match> findAll(Pageable pageable) {
        Page<MatchEntity> entityPage = jpaRepository.findAll(pageable);
        return entityPage.map(mapper::fromEntity);
    }

    public Page<Match> findByCriteria(MatchSearchCriteria criteria, Pageable pageable) {
        Specification<MatchEntity> specification = specificationBuilder.build(criteria);
        return jpaRepository.findAll(specification, pageable)
                .map(mapper::fromEntity);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
