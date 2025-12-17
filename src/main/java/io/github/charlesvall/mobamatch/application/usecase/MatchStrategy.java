package io.github.charlesvall.mobamatch.application.usecase;

import io.github.charlesvall.mobamatch.domain.matching.MatchRule;
import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.port.in.MatchDomainStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MatchStrategy implements MatchDomainStrategy {
    private final List<MatchRule> rules;

    @Override
    public boolean isMatchValid(Match match, List<Player> playerList) {
        return rules.stream().allMatch(rule -> rule.validate(match, playerList));
    }
}
