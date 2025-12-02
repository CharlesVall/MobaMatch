package io.github.charlesvall.mobamatch.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Player {

    private String id;
    private String username;
    private int skillLevel;
    private Region region;
    private Role preferredRole;

    private Player(String username, int skillLevel, Region region, Role preferredRole) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.skillLevel = skillLevel;
        this.region = region;
        this.preferredRole = preferredRole;
    }

    public static Player of(String username, int skillLevel, Region region, Role preferredRole) {
        return new Player(username, skillLevel, region, preferredRole);
    }

    public void updateWith(Player other) {
        this.username = other.username;
        this.skillLevel = other.skillLevel;
        this.region = other.region;
        this.preferredRole = other.preferredRole;
    }

}
