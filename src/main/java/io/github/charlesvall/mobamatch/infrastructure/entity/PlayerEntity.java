package io.github.charlesvall.mobamatch.infrastructure.entity;

import io.github.charlesvall.mobamatch.domain.model.Region;
import io.github.charlesvall.mobamatch.domain.model.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;
    private int skillLevel;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private Role preferredRole;

    public PlayerEntity(String id, String username, int skillLevel, Region region, Role preferredRole) {
        this.id = id;
        this.username = username;
        this.skillLevel = skillLevel;
        this.region = region;
        this.preferredRole = preferredRole;
    }
}
