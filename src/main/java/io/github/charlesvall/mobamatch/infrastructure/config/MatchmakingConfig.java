package io.github.charlesvall.mobamatch.infrastructure.config;

import io.github.charlesvall.mobamatch.application.usecase.MatchStrategy;
import io.github.charlesvall.mobamatch.domain.matching.MatchRule;
import io.github.charlesvall.mobamatch.domain.matching.rules.AverageSkillRule;
import io.github.charlesvall.mobamatch.domain.matching.rules.RegionCompatibiltyRule;
import io.github.charlesvall.mobamatch.domain.matching.rules.RoleDistributionRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MatchmakingConfig {

    @Bean
    public MatchRule roleDistributionRule() {
        return new RoleDistributionRule();
    }

    @Bean
    public MatchRule averageSkillRule() {
        return new AverageSkillRule();
    }
    @Bean
    public MatchRule regionCompatibiltyRule() {
        return new RegionCompatibiltyRule();
    }

    @Bean
    public MatchStrategy matchStrategy(List<MatchRule> rules) {
        return new MatchStrategy(rules);
    }
}
