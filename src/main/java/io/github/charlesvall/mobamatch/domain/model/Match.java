package io.github.charlesvall.mobamatch.domain.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Match {

    @Id
    private String id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Player> playerList;

    private int averageSkill;

    @Enumerated(EnumType.STRING)
    private Region region;
    private LocalDateTime createdAt;

    private Match(List<Player> playerList, Region region) {
        this.id = java.util.UUID.randomUUID().toString();
        this.playerList = playerList;
        this.region = region;
        this.averageSkill = calculateAverageSkill(playerList);
        this.createdAt = LocalDateTime.now();
    }

    public static Match of(List<Player> playerList, Region region) {
        return new Match(playerList, region);
    }

    private int calculateAverageSkill(List<Player> playerList) {
        return (int) playerList.stream()
                .mapToInt(Player::getSkillLevel)
                .average()
                .orElse(0);
    }
}
