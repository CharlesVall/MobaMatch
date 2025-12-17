package io.github.charlesvall.mobamatch.infrastructure.entity;

import io.github.charlesvall.mobamatch.domain.model.Region;
import io.github.charlesvall.mobamatch.domain.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @Min(0) @Max(100)
    private int skillLevel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role preferredRole;

    @Column(name = "in_match", nullable = false)
    private boolean inMatch;
}