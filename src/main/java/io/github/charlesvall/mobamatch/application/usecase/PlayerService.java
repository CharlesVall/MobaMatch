package io.github.charlesvall.mobamatch.application.usecase;

import io.github.charlesvall.mobamatch.domain.exception.NotFoundException;
import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.model.PlayerSearchCriteria;
import io.github.charlesvall.mobamatch.domain.port.in.PlayerDomainService;
import io.github.charlesvall.mobamatch.domain.port.out.PlayerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerService implements PlayerDomainService {

    private final PlayerRepository playerRepository;

    @Override
    public Player createPlayer(Player playerData) {
        return playerRepository.save(playerData);
    }

    @Override
    public Optional<Player> findPlayerById(String id) {
        return playerRepository.findById(id);
    }

    @Override
    public Page<Player> findAllPlayer(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @Override
    public Page<Player> findByCriteria(PlayerSearchCriteria criteria, Pageable pageable) {
        return playerRepository.findByCriteria(criteria, pageable);
    }

    @Override
    public Player updateById(String id, Player player) {
        Player existing = playerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        existing.updateWith(player);
        return playerRepository.save(existing);
    }

    @Override
    public void deletePlayerById(String id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        playerRepository.deleteById(player.getId());
    }
}
