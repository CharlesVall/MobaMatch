package io.github.charlesvall.mobamatch.application.usecase;

import io.github.charlesvall.mobamatch.domain.exception.MatchIsNotValidException;
import io.github.charlesvall.mobamatch.domain.exception.NotFoundException;
import io.github.charlesvall.mobamatch.domain.model.*;
import io.github.charlesvall.mobamatch.domain.port.in.MatchDomainStrategy;
import io.github.charlesvall.mobamatch.domain.port.out.MatchRepository;
import io.github.charlesvall.mobamatch.domain.port.out.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MatchService - Use Cases")
class MatchServiceTest {

    private MatchService matchService;
    private FakeMatchRepository fakeMatchRepository;
    private FakePlayerRepository fakePlayerRepository;
    private FakeMatchStrategy fakeMatchStrategy;

    @BeforeEach
    void setUp() {
        fakeMatchRepository = new FakeMatchRepository();
        fakePlayerRepository = new FakePlayerRepository();
        fakeMatchStrategy = new FakeMatchStrategy();
        matchService = new MatchService(fakeMatchStrategy, fakeMatchRepository, fakePlayerRepository);
    }

    // ============================================
    // TESTS CREATE MATCH
    // ============================================

    @Nested
    @DisplayName("Create Match")
    class CreateMatch {

        @Test
        @DisplayName("Should create match with valid players")
        void shouldCreateMatchWithValidPlayers() {
            Player player1 = createAndSavePlayer("Faker", Role.MID);
            Player player2 = createAndSavePlayer("Uzi", Role.BOT);
            Player player3 = createAndSavePlayer("TheShy", Role.TOP);
            Player player4 = createAndSavePlayer("Ning", Role.JUNGLE);
            Player player5 = createAndSavePlayer("Meiko", Role.SUPPORT);

            Player player6 = createAndSavePlayer("Chovy", Role.MID);
            Player player7= createAndSavePlayer("Gumayusi", Role.BOT);
            Player player8 = createAndSavePlayer("Zeus", Role.TOP);
            Player player9 = createAndSavePlayer("Canyon", Role.JUNGLE);
            Player player10 = createAndSavePlayer("Keria", Role.SUPPORT);

            List<String> playerIds = List.of(
                    player1.getId(),
                    player2.getId(),
                    player3.getId(),
                    player4.getId(),
                    player5.getId(),
                    player6.getId(),
                    player7.getId(),
                    player8.getId(),
                    player9.getId(),
                    player10.getId()
            );

            Match created = matchService.createMatch(playerIds);

            assertThat(created).isNotNull();
            assertThat(created.getId()).isNotNull();
            assertThat(created.getPlayerIds()).hasSize(10);
            assertThat(fakeMatchRepository.storage).hasSize(1);
        }

        @Test
        @DisplayName("Should activate all players when creating match")
        void shouldActivateAllPlayersWhenCreatingMatch() {
            Player player1 = createAndSavePlayer("Faker", Role.MID);
            Player player2 = createAndSavePlayer("Uzi", Role.BOT);
            Player player3 = createAndSavePlayer("TheShy", Role.TOP);
            Player player4 = createAndSavePlayer("Ning", Role.JUNGLE);
            Player player5 = createAndSavePlayer("Meiko", Role.SUPPORT);

            Player player6 = createAndSavePlayer("Chovy", Role.MID);
            Player player7= createAndSavePlayer("Gumayusi", Role.BOT);
            Player player8 = createAndSavePlayer("Zeus", Role.TOP);
            Player player9 = createAndSavePlayer("Canyon", Role.JUNGLE);
            Player player10 = createAndSavePlayer("Keria", Role.SUPPORT);

            List<String> playerIds = List.of(
                    player1.getId(),
                    player2.getId(),
                    player3.getId(),
                    player4.getId(),
                    player5.getId(),
                    player6.getId(),
                    player7.getId(),
                    player8.getId(),
                    player9.getId(),
                    player10.getId()
            );

            matchService.createMatch(playerIds);

            playerIds.forEach(playerId -> {
                Player player = fakePlayerRepository.findById(playerId).orElseThrow();
                assertThat(player.isInMatch()).isTrue();
            });
        }

        @Test
        @DisplayName("Should throw NotFoundException when player not found")
        void shouldThrowNotFoundExceptionWhenPlayerNotFound() {
            List<String> playerIds = List.of("nonexistent-id");

            assertThatThrownBy(() -> matchService.createMatch(playerIds))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("nonexistent-id");
        }

        @Test
        @DisplayName("Should throw MatchIsNotValidException when match strategy rejects")
        void shouldThrowMatchIsNotValidExceptionWhenMatchStrategyRejects() {
            Player player1 = createAndSavePlayer("Faker", Role.MID);
            Player player2 = createAndSavePlayer("Uzi", Role.BOT);

            List<String> playerIds = List.of(player1.getId(), player2.getId());

            fakeMatchStrategy.shouldValidate = false;

            assertThatThrownBy(() -> matchService.createMatch(playerIds))
                    .isInstanceOf(MatchIsNotValidException.class);
        }

        @Test
        @DisplayName("Should not save match when strategy rejects")
        void shouldNotSaveMatchWhenStrategyRejects() {
            Player player1 = createAndSavePlayer("Faker", Role.MID);
            Player player2 = createAndSavePlayer("Uzi", Role.BOT);

            List<String> playerIds = List.of(player1.getId(), player2.getId());

            fakeMatchStrategy.shouldValidate = false;

            try {
                matchService.createMatch(playerIds);
            } catch (MatchIsNotValidException e) {
            }

            assertThat(fakeMatchRepository.storage).isEmpty();
        }

        @Test
        @DisplayName("Should handle partial player not found correctly")
        void shouldHandlePartialPlayerNotFoundCorrectly() {
            Player player1 = createAndSavePlayer("Faker", Role.MID);

            List<String> playerIds = List.of(player1.getId(), "nonexistent-id");

            assertThatThrownBy(() -> matchService.createMatch(playerIds))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("nonexistent-id");

            assertThat(fakeMatchRepository.storage).isEmpty();
        }
    }

    // ============================================
    // TESTS FIND BY ID
    // ============================================

    @Nested
    @DisplayName("Find Match By ID")
    class FindMatchById {

        @Test
        @DisplayName("Should find existing match")
        void shouldFindExistingMatch() {
            Match match = createAndSaveMatch();

            Optional<Match> found = matchService.findMatchById(match.getId());

            assertThat(found).isPresent();
            assertThat(found.get().getId()).isEqualTo(match.getId());
        }

        @Test
        @DisplayName("Should return empty when match not found")
        void shouldReturnEmptyWhenMatchNotFound() {
            Optional<Match> found = matchService.findMatchById("nonexistent");

            assertThat(found).isEmpty();
        }
    }

    // ============================================
    // TESTS FIND ALL
    // ============================================

    @Nested
    @DisplayName("Find All Matches")
    class FindAllMatches {

        @Test
        @DisplayName("Should return empty page when no matches")
        void shouldReturnEmptyPageWhenNoMatches() {
            Pageable pageable = PageRequest.of(0, 20);

            Page<Match> result = matchService.findAllMatch(pageable);

            assertThat(result).isEmpty();
            assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Should return all matches")
        void shouldReturnAllMatches() {
            createAndSaveMatch();
            createAndSaveMatch();
            createAndSaveMatch();

            Pageable pageable = PageRequest.of(0, 20);

            Page<Match> result = matchService.findAllMatch(pageable);

            assertThat(result).hasSize(3);
            assertThat(result.getTotalElements()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should paginate correctly")
        void shouldPaginateCorrectly() {
            for (int i = 0; i < 25; i++) {
                createAndSaveMatch();
            }

            Pageable pageable = PageRequest.of(0, 10);

            Page<Match> result = matchService.findAllMatch(pageable);

            assertThat(result).hasSize(10);
            assertThat(result.getTotalElements()).isEqualTo(25);
            assertThat(result.getTotalPages()).isEqualTo(3);
        }
    }

    // ============================================
    // TESTS UPDATE
    // ============================================

    @Nested
    @DisplayName("Update Match")
    class UpdateMatch {

        @Test
        @DisplayName("Should update existing match")
        void shouldUpdateExistingMatch() {
            Match match = createAndSaveMatch();
            Match updates = createTestMatch();

            Match updated = matchService.updateById(match.getId(), updates);

            assertThat(updated).isNotNull();
            assertThat(updated.getId()).isEqualTo(match.getId());
        }

        @Test
        @DisplayName("Should throw NotFoundException when match not exists")
        void shouldThrowNotFoundExceptionWhenMatchNotExists() {
            Match updates = createTestMatch();

            assertThatThrownBy(() -> matchService.updateById("nonexistent", updates))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("nonexistent");
        }

        @Test
        @DisplayName("Should preserve match ID when updating")
        void shouldPreserveMatchIdWhenUpdating() {
            Match match = createAndSaveMatch();
            String originalId = match.getId();
            Match updates = createTestMatch();

            Match updated = matchService.updateById(originalId, updates);

            assertThat(updated.getId()).isEqualTo(originalId);
        }
    }

    // ============================================
    // TESTS DELETE
    // ============================================

    @Nested
    @DisplayName("Delete Match")
    class DeleteMatch {

        @Test
        @DisplayName("Should delete existing match")
        void shouldDeleteExistingMatch() {
            Match match = createAndSaveMatch();

            matchService.deleteMatchById(match.getId());

            assertThat(fakeMatchRepository.storage).isEmpty();
            assertThat(matchService.findMatchById(match.getId())).isEmpty();
        }

        @Test
        @DisplayName("Should deactivate all players when deleting match")
        void shouldDeactivateAllPlayersWhenDeletingMatch() {
            Player player1 = createAndSavePlayer("Faker", Role.MID);
            Player player2 = createAndSavePlayer("Uzi", Role.BOT);

            player1.activate();
            player2.activate();
            fakePlayerRepository.save(player1);
            fakePlayerRepository.save(player2);

            Match match = Match.of(
                    List.of(player1, player2),
                    Region.EUW
            );
            match = fakeMatchRepository.save(match);

            matchService.deleteMatchById(match.getId());

            Player updatedPlayer1 = fakePlayerRepository.findById(player1.getId()).orElseThrow();
            Player updatedPlayer2 = fakePlayerRepository.findById(player2.getId()).orElseThrow();

            assertThat(updatedPlayer1.isInMatch()).isFalse();
            assertThat(updatedPlayer2.isInMatch()).isFalse();
        }

        @Test
        @DisplayName("Should throw NotFoundException when deleting non-existent match")
        void shouldThrowNotFoundExceptionWhenDeletingNonExistentMatch() {
            assertThatThrownBy(() -> matchService.deleteMatchById("nonexistent"))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("nonexistent");
        }

        @Test
        @DisplayName("Should not affect other matches when deleting")
        void shouldNotAffectOtherMatchesWhenDeleting() {
            Match match1 = createAndSaveMatch();
            Match match2 = createAndSaveMatch();

            matchService.deleteMatchById(match1.getId());

            assertThat(fakeMatchRepository.storage).hasSize(1);
            assertThat(matchService.findMatchById(match2.getId())).isPresent();
        }

        @Test
        @DisplayName("Should throw NotFoundException when player not found during deletion")
        void shouldThrowNotFoundExceptionWhenPlayerNotFoundDuringDeletion() {
            Match match = Match.of(
                    List.of(),
                    Region.EUW
            );
            match.setPlayerIds(List.of("nonexistent-player-id"));
            match = fakeMatchRepository.save(match);

            String matchId = match.getId();
            assertThatThrownBy(() -> matchService.deleteMatchById(matchId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("nonexistent-player-id");
        }
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private Player createAndSavePlayer(String username, Role role) {
        Player player = Player.of(username, 50, Region.EUW, role);
        return fakePlayerRepository.save(player);
    }

    private Match createTestMatch() {
        Player p1 = createAndSavePlayer("Player1", Role.TOP);
        Player p2 = createAndSavePlayer("Player2", Role.JUNGLE);
        Player p3 = createAndSavePlayer("Player3", Role.MID);
        Player p4 = createAndSavePlayer("Player4", Role.BOT);
        Player p5 = createAndSavePlayer("Player5", Role.SUPPORT);
        Player p6 = createAndSavePlayer("Player6", Role.TOP);
        Player p7 = createAndSavePlayer("Player7", Role.JUNGLE);
        Player p8 = createAndSavePlayer("Player8", Role.MID);
        Player p9 = createAndSavePlayer("Player9", Role.BOT);
        Player p10 = createAndSavePlayer("Player10", Role.SUPPORT);

        return Match.of(
                List.of(p1, p2, p3, p4, p5,
                        p6, p7, p8, p9, p10),
                Region.EUW
        );
    }

    private Match createAndSaveMatch() {
        Match match = createTestMatch();
        return fakeMatchRepository.save(match);
    }

    // ============================================
    // FAKE MATCH REPOSITORY
    // ============================================

    private static class FakeMatchRepository implements MatchRepository {

        public final Map<String, Match> storage = new HashMap<>();

        @Override
        public Match save(Match match) {
            if (match.getId() == null) {
                match.setId(UUID.randomUUID().toString());
            }
            storage.put(match.getId(), match);
            return match;
        }

        @Override
        public Optional<Match> findById(String id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public Page<Match> findAll(Pageable pageable) {
            List<Match> allMatches = new ArrayList<>(storage.values());

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), allMatches.size());

            List<Match> pageContent = allMatches.subList(start, end);

            return new PageImpl<>(pageContent, pageable, allMatches.size());
        }

        @Override
        public Page<Match> findByCriteria(MatchSearchCriteria criteria, Pageable pageable) {
            List<Match> filtered = storage.values().stream()
                    .filter(match -> matchesCriteria(match, criteria))
                    .toList();

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), filtered.size());

            List<Match> pageContent = filtered.subList(start, end);

            return new PageImpl<>(pageContent, pageable, filtered.size());
        }

        private boolean matchesCriteria(Match match, MatchSearchCriteria criteria) {
            if (criteria.getRegion() != null &&
                    !match.getRegion().equals(criteria.getRegion())) {
                return false;
            }

            return true;
        }

        @Override
        public void deleteById(String id) {
            storage.remove(id);
        }
    }

    // ============================================
    // FAKE PLAYER REPOSITORY
    // ============================================

    private static class FakePlayerRepository implements PlayerRepository {

        public final Map<String, Player> storage = new HashMap<>();

        @Override
        public Player save(Player player) {
            if (player.getId() == null) {
                player.setId(UUID.randomUUID().toString());
            }
            storage.put(player.getId(), player);
            return player;
        }

        @Override
        public Optional<Player> findById(String id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public Page<Player> findAll(Pageable pageable) {
            List<Player> allPlayers = new ArrayList<>(storage.values());

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), allPlayers.size());

            List<Player> pageContent = allPlayers.subList(start, end);

            return new PageImpl<>(pageContent, pageable, allPlayers.size());
        }

        @Override
        public Page<Player> findByCriteria(PlayerSearchCriteria criteria, Pageable pageable) {
            throw new UnsupportedOperationException("Not needed for match tests");
        }

        @Override
        public void deleteById(String id) {
            storage.remove(id);
        }
    }

    // ============================================
    // FAKE MATCH STRATEGY
    // ============================================

    private static class FakeMatchStrategy implements MatchDomainStrategy {

        public boolean shouldValidate = true;

        @Override
        public boolean isMatchValid(Match match, List<Player> players) {
            return shouldValidate;
        }
    }
}