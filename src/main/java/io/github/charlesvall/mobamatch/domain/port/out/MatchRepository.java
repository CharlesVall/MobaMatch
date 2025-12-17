package io.github.charlesvall.mobamatch.domain.port.out;

import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.MatchSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MatchRepository {
    Match save(Match match);
    Optional<Match> findById(String id);
    Page<Match> findAll(Pageable pageable);
    Page<Match> findByCriteria(MatchSearchCriteria criteria, Pageable pageable);
    void deleteById(String id);
}
