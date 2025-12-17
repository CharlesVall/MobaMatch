package io.github.charlesvall.mobamatch.domain.matching.rules;

import io.github.charlesvall.mobamatch.domain.matching.MatchRule;
import io.github.charlesvall.mobamatch.domain.model.Match;
import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.model.Role;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RoleDistributionRule implements MatchRule {

    @Override
    public boolean validate(Match match, List<Player> playerList) {

        Map<Role, Integer> counts = new EnumMap<>(Role.class);

        playerList.forEach(player ->
                counts.merge(player.getPreferredRole(), 1, Integer::sum)
        );

        return Arrays.stream(Role.values())
                .allMatch(role -> counts.getOrDefault(role, 0) == 2);
    }

}
