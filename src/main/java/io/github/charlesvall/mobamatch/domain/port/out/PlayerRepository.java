package io.github.charlesvall.mobamatch.domain.port.out;

import io.github.charlesvall.mobamatch.domain.model.Player;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository {
    Player save(Player player);
    Optional<Player> findById(String id);
    List<Player> findAll();
    void deleteById(String id);
}