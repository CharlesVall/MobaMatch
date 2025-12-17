package io.github.charlesvall.mobamatch.domain.matching.rules;

import io.github.charlesvall.mobamatch.domain.matching.MatchRule;
import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.Player;

import java.util.List;

public class RegionCompatibiltyRule implements MatchRule {

    @Override
    public boolean validate(Match match, List<Player> playerList) {
        return playerList.stream()
                .map(Player::getRegion)
                .distinct()
                .count() == 1;
    }
}