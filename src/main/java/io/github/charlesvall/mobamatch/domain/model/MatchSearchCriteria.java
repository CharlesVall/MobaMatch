package io.github.charlesvall.mobamatch.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchSearchCriteria {
    private Region region;
    private Integer minAverageLevel;
    private Integer maxAverageLevel;

    public boolean isEmpty() {
        return  (region == null || region.toString().isBlank()) &&
                minAverageLevel == null &&
                maxAverageLevel == null;
    }
}
