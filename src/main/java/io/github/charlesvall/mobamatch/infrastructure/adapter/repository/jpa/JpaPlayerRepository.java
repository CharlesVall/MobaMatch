package io.github.charlesvall.mobamatch.infrastructure.adapter.repository.jpa;

import io.github.charlesvall.mobamatch.infrastructure.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaPlayerRepository extends
        JpaRepository<PlayerEntity, String>,
        JpaSpecificationExecutor<PlayerEntity> {
}