package io.github.charlesvall.mobamatch.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Player {

    private String id;
    private String username;
    private int skillLevel;
    private Region region;
    private Role preferredRole;
    private boolean inMatch;

    private Player(String username, int skillLevel, Region region, Role preferredRole) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.skillLevel = skillLevel;
        this.region = region;
        this.preferredRole = preferredRole;
        this.inMatch = false;
    }

    public static Player of(String username, int skillLevel, Region region, Role preferredRole) {
        PlayerValidate.validateUsername(username);
        PlayerValidate.validateSkillLevel(skillLevel);
        return new Player(username, skillLevel, region, preferredRole);
    }

    public void updateWith(Player other) {
        this.username = other.username;
        this.skillLevel = other.skillLevel;
        this.region = other.region;
        this.preferredRole = other.preferredRole;
    }

    public void deactivate() {
        if (!this.isInMatch()) {
            throw new IllegalStateException("Player is already inactive");
        }
        this.inMatch = false;
    }

    public void activate() {
        if (this.isInMatch()) {
            throw new IllegalStateException("Player is already active");
        }
        this.inMatch = true;
    }

}
