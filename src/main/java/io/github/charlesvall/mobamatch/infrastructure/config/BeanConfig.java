package io.github.charlesvall.mobamatch.infrastructure.config;

import io.github.charlesvall.mobamatch.application.usecase.PlayerService;
import io.github.charlesvall.mobamatch.domain.port.out.PlayerRepository;
import io.github.charlesvall.mobamatch.infrastructure.mapper.PlayerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PlayerService playerService(PlayerRepository playerRepository) {
        return new PlayerService(playerRepository);
    }
}
