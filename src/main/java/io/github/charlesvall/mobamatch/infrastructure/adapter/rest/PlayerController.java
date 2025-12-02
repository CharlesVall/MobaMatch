package io.github.charlesvall.mobamatch.infrastructure.adapter.rest;

import io.github.charlesvall.mobamatch.application.usecase.PlayerService;
import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.infrastructure.dto.*;
import io.github.charlesvall.mobamatch.infrastructure.exception.InvalidIdFormatException;
import io.github.charlesvall.mobamatch.infrastructure.mapper.PlayerMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    @PostMapping
    public ResponseEntity<PlayerResponseDto> createPlayer(@Valid @RequestBody PlayerBodyRequestDto request) {
        Player createdPlayer = playerService.createPlayer(playerMapper.toDomain(request));
        URI location = URI.create("/players/" + createdPlayer.getId());
        return ResponseEntity
                .created(location)
                .body(playerMapper.toResponseDto(createdPlayer));
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerResponseDto> getPlayerById(@PathVariable String playerId) {
        validateUUID(playerId);

        return playerService.findPlayerById(playerId)
                .map(playerMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<PlayerListDto> getPlayerList() {
        List<PlayerResponseDto> playerList = playerService.findAllPlayer().stream()
                .map(playerMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(new PlayerListDto(playerList.size(), playerList));
    }

    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerResponseDto> modifyPlayerById(@PathVariable String playerId, @Valid @RequestBody PlayerBodyRequestDto request) {
        validateUUID(playerId);

        Player updatedPlayer = playerService.updateById(playerId, playerMapper.toDomain(request));
        return ResponseEntity
                .ok(playerMapper.toResponseDto(updatedPlayer));
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<String> deletePlayerById(@PathVariable String playerId) {
        validateUUID(playerId);

        playerService.deletePlayerById(playerId);
        return ResponseEntity
                .ok("Player id delete sucessefuly: " + playerId );
    }

    private void validateUUID(String id) {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidIdFormatException(id);
        }
    }
}
