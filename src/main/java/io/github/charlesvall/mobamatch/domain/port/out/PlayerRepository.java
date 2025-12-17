package io.github.charlesvall.mobamatch.domain.port.out;

import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.model.PlayerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PlayerRepository {
    Player save(Player player);
    Optional<Player> findById(String id);
    Page<Player> findAll(Pageable pageable);
    Page<Player> findByCriteria(PlayerSearchCriteria criteria, Pageable pageable);
    void deleteById(String id);
}