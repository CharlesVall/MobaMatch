package io.github.charlesvall.mobamatch.domain.matching;

import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.Player;

import java.util.List;

public interface MatchRule {
    boolean validate(Match match, List<Player> playerList);
}
