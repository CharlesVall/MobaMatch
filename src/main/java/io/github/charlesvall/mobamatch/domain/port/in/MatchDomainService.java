package io.github.charlesvall.mobamatch.domain.port.in;

import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.MatchSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MatchDomainService {
    Match createMatch(List<String> playerIds);
    Optional<Match> findMatchById(String id);
    Page<Match> findAllMatch(Pageable pageable);
    Page<Match> findByCriteria(MatchSearchCriteria criteria, Pageable pageable);
    Match updateById(String id, Match match);
    void deleteMatchById(String id);
}
