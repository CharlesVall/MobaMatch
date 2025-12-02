package io.github.charlesvall.mobamatch.infrastructure.adapter.repository.springdata;

import io.github.charlesvall.mobamatch.infrastructure.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPlayerRepository extends JpaRepository<PlayerEntity, String> {
}