package io.github.charlesvall.mobamatch.domain.port.in;

import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.Player;

import java.util.List;

public interface MatchDomainStrategy {
    boolean isMatchValid(Match match, List<Player> playerList);
}
