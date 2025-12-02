package io.github.charlesvall.mobamatch.domain.port.out;

import io.github.charlesvall.mobamatch.domain.model.Match;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    void save(Match match);
    Optional<Match> findById(String id);
    List<Match> findAll();
    void deleteById(String id);
}
