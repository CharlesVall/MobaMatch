package io.github.charlesvall.mobamatch.domain.port.in;


import io.github.charlesvall.mobamatch.domain.model.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerDomainService {
    Player createPlayer(Player playerData);
    Optional<Player> findPlayerById(String id);
    List<Player> findAllPlayer();
    void deletePlayerById(String id);
}
