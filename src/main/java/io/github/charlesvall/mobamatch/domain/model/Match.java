package io.github.charlesvall.mobamatch.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Match {

    private String id;
    private List<String> playerIds;
    private int averageSkill;
    private Region region;

    private Match(
            List<String> playerIds,
            int averageSkill,
            Region region
    ) {
        this.playerIds = List.copyOf(playerIds);
        this.averageSkill = averageSkill;
        this.region = region;
    }

    public static Match of(List<Player> players, Region region) {
        int avgSkill = calculateAverageSkill(players);
        List<String> ids = players.stream()
                .map(Player::getId)
                .toList();

        return new Match(ids, avgSkill, region);
    }

    public void updateWith(Match other) {
        this.playerIds = other.getPlayerIds();
        this.region = other.getRegion();
        this.averageSkill = other.getAverageSkill();
    }

    private static int calculateAverageSkill(List<Player> players) {
        return (int) players.stream()
                .mapToInt(Player::getSkillLevel)
                .average()
                .orElse(0);
    }
}
