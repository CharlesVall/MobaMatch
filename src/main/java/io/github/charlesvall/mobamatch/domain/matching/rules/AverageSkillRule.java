package io.github.charlesvall.mobamatch.domain.matching.rules;

import io.github.charlesvall.mobamatch.domain.matching.MatchRule;
import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.Player;

import java.util.List;

public class AverageSkillRule implements MatchRule {

    @Override
    public boolean validate(Match match, List<Player> playerList) {

        int minSkill = match.getAverageSkill() - 10;
        int maxSkill = match.getAverageSkill() + 10;

        return playerList.stream()
                .map(Player::getSkillLevel)
                .allMatch(skill -> skill >= minSkill && skill <= maxSkill);
    }
}
