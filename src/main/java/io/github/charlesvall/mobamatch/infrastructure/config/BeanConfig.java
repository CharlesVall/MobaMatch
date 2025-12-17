package io.github.charlesvall.mobamatch.infrastructure.config;

import io.github.charlesvall.mobamatch.application.usecase.MatchService;
import io.github.charlesvall.mobamatch.application.usecase.MatchStrategy;
import io.github.charlesvall.mobamatch.application.usecase.PlayerService;
import io.github.charlesvall.mobamatch.domain.port.out.MatchRepository;
import io.github.charlesvall.mobamatch.domain.port.out.PlayerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PlayerService playerService(PlayerRepository playerRepository) {
        return new PlayerService(playerRepository);
    }

    @Bean
    public MatchService matchService(MatchRepository matchRepository,
                                     PlayerRepository playerRepository,
                                     MatchStrategy matchStrategy) {
        return new MatchService(matchStrategy, matchRepository, playerRepository);
    }
}
