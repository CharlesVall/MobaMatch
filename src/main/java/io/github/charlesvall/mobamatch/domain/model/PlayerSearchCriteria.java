package io.github.charlesvall.mobamatch.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerSearchCriteria {
    private String username;
    private Role preferredRole;
    private Region region;
    private Integer minLevel;
    private Integer maxLevel;

    public boolean isEmpty() {
        return (username == null || username.isBlank()) &&
                (preferredRole == null || preferredRole.toString().isBlank()) &&
                (region == null || region.toString().isBlank()) &&
                minLevel == null &&
                maxLevel == null;
    }
}