package io.github.charlesvall.mobamatch.infrastructure.adapter.rest;

import io.github.charlesvall.mobamatch.application.usecase.MatchService;
import io.github.charlesvall.mobamatch.domain.model.*;
import io.github.charlesvall.mobamatch.infrastructure.dto.*;
import io.github.charlesvall.mobamatch.infrastructure.mapper.match.MatchDtoMapper;
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
@RequestMapping("/matches")
@RequiredArgsConstructor
@Validated
@Tag(name = "Matches", description = "Match management endpoints")
public class MatchController {

    private final MatchService matchService;
    private final MatchDtoMapper matchDtoMapper;

    @PostMapping
    @Operation(summary = "Create a new match", description = "Creates a new match and returns the created resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Match created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Match already exists")
    })
    public ResponseEntity<MatchDto> createMatch(
            @Valid @RequestBody CreateMatchRequest request
    ) {
        Match createdMatch = matchService.createMatch(request.playerIds());
        MatchDto response = matchDtoMapper.toResponseDto(createdMatch);

        URI location = URI.create("/matches/" + createdMatch.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{matchId}")
    @Operation(summary = "Get match by ID", description = "Retrieves a match by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match found"),
            @ApiResponse(responseCode = "404", description = "Match not found")
    })
    public ResponseEntity<MatchDto> getPlayerById(
            @Parameter(description = "Match unique identifier", required = true)
            @PathVariable String matchId
    ) {
        return matchService.findMatchById(matchId)
                .map(matchDtoMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(
            summary = "Get paginated and filtered list of matches",
            description = """
                Retrieves matches with optional filtering and sorting.
                All filters are optional and can be combined.
                
                Available filters:
                - name: Filter by player name (case-insensitive, contains)
                - region: Filter by region (EUW, EUNE, NA, RU, TK, CN, KR, JP)
                - minLevel: Filter by minimum level (1-100)
                - maxLevel: Filter by maximum level (1-100)
                
                Sorting:
                - Use 'sort' parameter with format: field,direction
                - Multiple sorts: field1,direction1;field2,direction2
                - Available fields: username, preferredRole, skillLevel
                - Directions: asc, desc
                
                Examples:
                - ?region=KR
                - ?region=EUNE&minAverageLevel=42
                - ?region=JP&maxAverageLevel=100&sort=averageSkill,desc
                """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matches retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
    })
    public ResponseEntity<PageDto<MatchDto>> getAllMatches(
            @ParameterObject
            @PageableDefault(size = 5, sort = "averageSkill", direction = Sort.Direction.ASC) Pageable pageable,

            @Parameter(description = "Filter by region")
            @RequestParam(required = false) Region region,

            @Parameter(description = "Filter by minimum average level (1-100)")
            @RequestParam(required = false) @Min(1) @Max(100) Integer minAverageLevel,

            @Parameter(description = "Filter by maximum average level (1-100)")
            @RequestParam(required = false) @Max(100) Integer maxAverageLevel
    ) {
        MatchSearchCriteria criteria = MatchSearchCriteria.builder()
                .region(region)
                .minAverageLevel(minAverageLevel)
                .maxAverageLevel(maxAverageLevel)
                .build();

        Page<MatchDto> dtoPage;
        if (criteria.isEmpty()) {
            dtoPage = matchService.findAllMatch(pageable)
                    .map(matchDtoMapper::toResponseDto);
        } else {
            dtoPage = matchService.findByCriteria(criteria, pageable)
                    .map(matchDtoMapper::toResponseDto);
        }

        return ResponseEntity.ok(PageDto.of(dtoPage));
    }


    @DeleteMapping("/{matchId}")
    @Operation(summary = "Delete match", description = "Delete a match by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Match deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Match not found")
    })
    public ResponseEntity<String> deleteMatchById(
            @Parameter(description = "Match unique identifier", required = true)
            @PathVariable String matchId
    ) {
        matchService.deleteMatchById(matchId);
        return ResponseEntity
                .ok("Match deleted successefully from id: " + matchId );
    }
}