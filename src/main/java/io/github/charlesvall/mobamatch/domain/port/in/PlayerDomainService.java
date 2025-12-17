package io.github.charlesvall.mobamatch.domain.port.in;


import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.model.PlayerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PlayerDomainService {
    Player createPlayer(Player playerData);
    Optional<Player> findPlayerById(String id);
    Page<Player> findAllPlayer(Pageable pageable);
    Page<Player> findByCriteria(PlayerSearchCriteria criteria, Pageable pageable);
    Player updateById(String id, Player player);
    void deletePlayerById(String id);
}
