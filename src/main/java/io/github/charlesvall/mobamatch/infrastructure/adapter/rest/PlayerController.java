package io.github.charlesvall.mobamatch.infrastructure.adapter.rest;

import io.github.charlesvall.mobamatch.application.usecase.PlayerService;
import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.model.PlayerSearchCriteria;
import io.github.charlesvall.mobamatch.domain.model.Region;
import io.github.charlesvall.mobamatch.domain.model.Role;
import io.github.charlesvall.mobamatch.infrastructure.dto.*;

import io.github.charlesvall.mobamatch.infrastructure.mapper.player.PlayerDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
@Validated
@Tag(name = "Players", description = "Player management endpoints")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerDtoMapper playerDtoMapper;

    @PostMapping
    @Operation(summary = "Create a new player", description = "Creates a new player and returns the created resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Player already exists")
    })
    public ResponseEntity<PlayerResponseDto> createPlayer(
            @Valid @RequestBody PlayerRequestDto request
    ) {
        Player playerRequest = playerDtoMapper.toDomain(request);
        Player createdPlayer = playerService.createPlayer(playerRequest);
        PlayerResponseDto response = playerDtoMapper.toResponseDto(createdPlayer);

        URI location = URI.create("/players/" + createdPlayer.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{playerId}")
    @Operation(summary = "Get player by ID", description = "Retrieves a player by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player found"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<PlayerResponseDto> getPlayerById(
            @Parameter(description = "Player unique identifier", required = true)
            @PathVariable String playerId
    ) {
        return playerService.findPlayerById(playerId)
                .map(playerDtoMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(
            summary = "Get paginated and filtered list of players",
            description = """
                Retrieves players with optional filtering and sorting.
                All filters are optional and can be combined.
                
                Available filters:
                - name: Filter by player name (case-insensitive, contains)
                - position: Filter by preferred role (TOP, JUNGLE, MID, BOT, SUPPORT)
                - region: Filter by region (EUW, EUNE, NA, RU, TK, CN, KR, JP)
                - minLevel: Filter by minimum level (1-100)
                - maxLevel: Filter by maximum level (1-100)
                
                Sorting:
                - Use 'sort' parameter with format: field,direction
                - Multiple sorts: field1,direction1;field2,direction2
                - Available fields: username, preferredRole, skillLevel
                - Directions: asc, desc
                
                Examples:
                - ?username=faker
                - ?preferredRole=Mid&minLevel=25
                - ?username=shadow&maxLevel=100&sort=skillLevel,desc
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Players retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
    })
    public ResponseEntity<PageDto<PlayerResponseDto>> getAllPlayers(
            @ParameterObject
            @PageableDefault(size = 5, sort = "username", direction = Sort.Direction.ASC) Pageable pageable,

            @Parameter(description = "Filter by player username (contains, case-insensitive)")
            @RequestParam(required = false) String username,

            @Parameter(description = "Filter by preferred role")
            @RequestParam(required = false) Role preferredRole,

            @Parameter(description = "Filter by region")
            @RequestParam(required = false) Region region,

            @Parameter(description = "Filter by minimum level (1-100)")
            @RequestParam(required = false) @Min(1) @Max(100) Integer minLevel,

            @Parameter(description = "Filter by maximum level (1-100)")
            @RequestParam(required = false) @Max(100) Integer maxLevel
    ) {
        PlayerSearchCriteria criteria = PlayerSearchCriteria.builder()
                .username(username)
                .preferredRole(preferredRole)
                .region(region)
                .minLevel(minLevel)
                .maxLevel(maxLevel)
                .build();

        Page<PlayerResponseDto> dtoPage;
        if (criteria.isEmpty()) {
            dtoPage = playerService.findAllPlayer(pageable)
                    .map(playerDtoMapper::toResponseDto);
        } else {
            dtoPage = playerService.findByCriteria(criteria, pageable)
                    .map(playerDtoMapper::toResponseDto);
        }

        return ResponseEntity.ok(PageDto.of(dtoPage));
    }

    @PutMapping("/{playerId}")
    @Operation(summary = "Update player", description = "Updates an existing player by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<PlayerResponseDto> updatePlayerById(
            @Parameter(description = "Player unique identifier", required = true)
            @PathVariable String playerId,

            @Valid @RequestBody PlayerRequestDto request
    ) {
        Player updateData = playerDtoMapper.toDomain(request);
        Player updatedPlayer = playerService.updateById(playerId, updateData);
        PlayerResponseDto response = playerDtoMapper.toResponseDto(updatedPlayer);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{playerId}")
    @Operation(summary = "Delete player", description = "Delete a player by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Player deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<String> deletePlayerById(
            @Parameter(description = "Player unique identifier", required = true)
            @PathVariable String playerId
    ) {
        playerService.deletePlayerById(playerId);
        return ResponseEntity
                .ok("Player deleted successefully from id: " + playerId );
    }
}
