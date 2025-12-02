package io.github.charlesvall.mobamatch.application.usecase;

import io.github.charlesvall.mobamatch.domain.exception.PlayerNotFoundException;
import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.port.in.PlayerDomainService;
import io.github.charlesvall.mobamatch.domain.port.out.PlayerRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PlayerService implements PlayerDomainService {

    private final PlayerRepository playerRepository;

    public Player createPlayer(Player playerData) {
        return playerRepository.save(playerData);
    }

    public Optional<Player> findPlayerById(String id) {
        return playerRepository.findById(id);
    }

    public List<Player> findAllPlayer() {
        return playerRepository.findAll();
    }

    public Player updateById(String id, Player player) {
        Player existing = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        existing.updateWith(player);

        return playerRepository.save(existing);
    }

    public void deletePlayerById(String id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));
        playerRepository.deleteById(player.getId());
    }
}
