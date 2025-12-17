package io.github.charlesvall.mobamatch.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerFilter {

    private String name;
    private String position;
    private Integer minLevel;
    private Integer maxLevel;

    public String toRSQL() {
        StringBuilder rsql = new StringBuilder();

        if (name != null && !name.isBlank()) {
            rsql.append("name=like=*").append(name).append("*");
        }

        if (position != null && !position.isBlank()) {
            if (rsql.length() > 0) rsql.append(";");
            rsql.append("position==").append(position);
        }

        if (minLevel != null) {
            if (rsql.length() > 0) rsql.append(";");
            rsql.append("level>=").append(minLevel);
        }

        if (maxLevel != null) {
            if (rsql.length() > 0) rsql.append(";");
            rsql.append("level<=").append(maxLevel);
        }

        return rsql.toString();
    }

    public Boolean isEmpty() {
        return (name == null || name.isBlank()) &&
                (position == null || position.isBlank()) &&
                minLevel == null &&
                maxLevel == null;
    }
}
