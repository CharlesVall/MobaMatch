package io.github.charlesvall.mobamatch.infrastructure.adapter.repository.jpa;

import io.github.charlesvall.mobamatch.infrastructure.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaMatchRepository extends
        JpaRepository<MatchEntity, String>,
        JpaSpecificationExecutor<MatchEntity> {
}