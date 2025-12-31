package io.github.charlesvall.mobamatch.application.usecase;

import io.github.charlesvall.mobamatch.domain.exception.MatchIsNotValidException;
import io.github.charlesvall.mobamatch.domain.exception.NotFoundException;
import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.MatchSearchCriteria;
import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.model.Region;
import io.github.charlesvall.mobamatch.domain.port.in.MatchDomainService;
import io.github.charlesvall.mobamatch.domain.port.in.MatchDomainStrategy;
import io.github.charlesvall.mobamatch.domain.port.out.MatchRepository;
import io.github.charlesvall.mobamatch.domain.port.out.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MatchService implements MatchDomainService {

    private final MatchDomainStrategy matchStrategy;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    @Override
    public Match createMatch(List<String> playerIds) {
        List<Player> players = playerIds.stream()
                .map(id -> playerRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(id)))
                .toList();

        Match match = Match.of(players, Region.EUW);

        if (!matchStrategy.isMatchValid(match, players)) {
            throw new MatchIsNotValidException();
        }

        players.forEach(player -> {
            player.activate();
            playerRepository.save(player);
        });

        return matchRepository.save(match);
    }

    @Override
    public Optional<Match> findMatchById(String id) {
        return matchRepository.findById(id);
    }

    @Override
    public Page<Match> findAllMatch(Pageable pageable) {
        return matchRepository.findAll(pageable);
    }

    @Override
    public Page<Match> findByCriteria(MatchSearchCriteria criteria, Pageable pageable) {
        return matchRepository.findByCriteria(criteria, pageable);
    }

    @Override
    public Match updateById(String id, Match match) {
        Match existing = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        existing.updateWith(match);
        return matchRepository.save(existing);
    }

    @Override
    public void deleteMatchById(String id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        List<Player> players = match.getPlayerIds().stream()
                .map(playerId -> playerRepository.findById(playerId)
                        .orElseThrow(() -> new NotFoundException(playerId)))
                .toList();

        players.forEach(player -> {
            player.deactivate();
            playerRepository.save(player);
        });

        matchRepository.deleteById(match.getId());
    }
}
