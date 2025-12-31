package io.github.charlesvall.mobamatch.application.usecase;

import io.github.charlesvall.mobamatch.domain.exception.NotFoundException;
import io.github.charlesvall.mobamatch.domain.model.Player;
import io.github.charlesvall.mobamatch.domain.model.PlayerSearchCriteria;
import io.github.charlesvall.mobamatch.domain.model.Region;
import io.github.charlesvall.mobamatch.domain.model.Role;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("PlayerService - Use Cases")
class PlayerServiceTest {

    private PlayerService playerService;
    private FakePlayerRepository fakeRepository;

    @BeforeEach
    void setUp() {
        fakeRepository = new FakePlayerRepository();
        playerService = new PlayerService(fakeRepository);
    }

    // ============================================
    // TESTS CREATE
    // ============================================

    @Nested
    @DisplayName("Create Player")
    class CreatePlayer {

        @Test
        @DisplayName("Should create player successfully")
        void shouldCreatePlayerSuccessfully() {
            Player player = createTestPlayer("Faker");

            Player created = playerService.createPlayer(player);

            assertThat(created).isNotNull();
            assertThat(created.getId()).isNotNull();
            assertThat(fakeRepository.storage.size() == 1);
        }
    }

    // ============================================
    // TESTS FIND BY ID
    // ============================================

    @Nested
    @DisplayName("Find Player By ID")
    class FindPlayerById {

        @Test
        @DisplayName("Should find existing player")
        void shouldFindExistingPlayer() {
            Player player = createTestPlayer("Faker");
            Player saved = playerService.createPlayer(player);

            Optional<Player> found = playerService.findPlayerById(saved.getId());

            assertThat(found.isPresent());
            assertThat(found.get().getId()).isEqualTo(saved.getId());
        }

        @Test
        @DisplayName("Should return empty when player not found")
        void shouldReturnEmptyWhenPlayerNotFound() {
            Optional<Player> found = playerService.findPlayerById("nonexistent");

            assertThat(found.isEmpty());
        }
    }

    // ============================================
    // TESTS FIND ALL
    // ============================================

    @Nested
    @DisplayName("Find All Players")
    class FindAllPlayers {

        @Test
        @DisplayName("Should return empty page when no players")
        void shouldReturnEmptyPageWhenNoPlayers() {
            Pageable pageable = PageRequest.of(0, 20);

            Page<Player> result = playerService.findAllPlayer(pageable);

            assertThat(result.isEmpty());
            assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Should return all players")
        void shouldReturnAllPlayers() {
            playerService.createPlayer(createTestPlayer("Faker"));
            playerService.createPlayer(createTestPlayer("Uzi"));
            playerService.createPlayer(createTestPlayer("TheShy"));

            Pageable pageable = PageRequest.of(0, 20);

            Page<Player> result = playerService.findAllPlayer(pageable);

            assertThat(result.getSize() == 3);
            assertThat(result.getTotalElements()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should paginate correctly")
        void shouldPaginateCorrectly() {
            for (int i = 0; i < 25; i++) {
                playerService.createPlayer(createTestPlayer("Player" + i));
            }

            Pageable pageable = PageRequest.of(0, 10);

            Page<Player> result = playerService.findAllPlayer(pageable);

            assertThat(result.getSize() == 10);
            assertThat(result.getTotalElements()).isEqualTo(25);
            assertThat(result.getTotalPages()).isEqualTo(3);
        }
    }

    // ============================================
    // TESTS SEARCH WITH CRITERIA
    // ============================================

    @Nested
    @DisplayName("Find By Criteria")
    class FindByCriteria {

        @Test
        @DisplayName("Should find players by criteria")
        void shouldFindPlayersByCriteria() {
            playerService.createPlayer(createTestPlayer("Faker"));
            playerService.createPlayer(createTestPlayer("Shadow"));
            playerService.createPlayer(createTestPlayer("Uzi"));

            PlayerSearchCriteria criteria = PlayerSearchCriteria.builder()
                    .username("fake")
                    .build();

            Pageable pageable = PageRequest.of(0, 20);

            Page<Player> result = playerService.findByCriteria(criteria, pageable);

            assertThat(result.getSize() == 1);
            assertThat(result.getContent().get(0).getUsername()).contains("Faker");
        }

        @Test
        @DisplayName("Should return empty when no match")
        void shouldReturnEmptyWhenNoMatch() {
            playerService.createPlayer(createTestPlayer("Faker"));

            PlayerSearchCriteria criteria = PlayerSearchCriteria.builder()
                    .username("nonexistent")
                    .build();

            Pageable pageable = PageRequest.of(0, 20);

            Page<Player> result = playerService.findByCriteria(criteria, pageable);

            assertThat(result.isEmpty());
        }
    }

    // ============================================
    // TESTS UPDATE
    // ============================================

    @Nested
    @DisplayName("Update Player")
    class UpdatePlayer {

        @Test
        @DisplayName("Should update existing player")
        void shouldUpdateExistingPlayer() {
            Player player = createTestPlayer("Faker");
            Player saved = playerService.createPlayer(player);

            Player updates = createTestPlayer("FakerUpdated");

            Player updated = playerService.updateById(saved.getId(), updates);

            assertThat(updated.getId()).isEqualTo(saved.getId());
            assertThat(updated.getUsername()).isEqualTo("FakerUpdated");
        }

        @Test
        @DisplayName("Should throw NotFoundException when player not exists")
        void shouldThrowNotFoundExceptionWhenPlayerNotExists() {
            Player updates = createTestPlayer("Faker");

            assertThatThrownBy(() -> playerService.updateById("nonexistent", updates))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("nonexistent");
        }

        @Test
        @DisplayName("Should preserve player ID when updating")
        void shouldPreservePlayerIdWhenUpdating() {
            Player player = createTestPlayer("Faker");
            Player saved = playerService.createPlayer(player);
            String originalId = saved.getId();

            Player updates = createTestPlayer("FakerUpdated");

            Player updated = playerService.updateById(originalId, updates);

            assertThat(updated.getId()).isEqualTo(originalId);
        }
    }

    // ============================================
    // TESTS DELETE
    // ============================================

    @Nested
    @DisplayName("Delete Player")
    class DeletePlayer {

        @Test
        @DisplayName("Should delete existing player")
        void shouldDeleteExistingPlayer() {
            Player player = createTestPlayer("Faker");
            Player saved = playerService.createPlayer(player);

            playerService.deletePlayerById(saved.getId());

            assertThat(fakeRepository.storage.isEmpty());
            assertThat(playerService.findPlayerById(saved.getId())).isEmpty();
        }

        @Test
        @DisplayName("Should throw NotFoundException when deleting non-existent player")
        void shouldThrowNotFoundExceptionWhenDeletingNonExistentPlayer() {
            assertThatThrownBy(() -> playerService.deletePlayerById("nonexistent"))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("nonexistent");
        }

        @Test
        @DisplayName("Should not affect other players when deleting")
        void shouldNotAffectOtherPlayersWhenDeleting() {
            Player player1 = playerService.createPlayer(createTestPlayer("Faker"));
            Player player2 = playerService.createPlayer(createTestPlayer("Uzi"));

            playerService.deletePlayerById(player1.getId());

            assertThat(fakeRepository.storage.size() == 1);
            assertThat(playerService.findPlayerById(player2.getId())).isPresent();
        }
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    private Player createTestPlayer(String username) {
        return Player.of(
                username,
                90,
                Region.EUW,
                Role.MID
        );
    }

    // ============================================
    // FAKE REPOSITORY
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
            List<Player> filtered = storage.values().stream()
                    .filter(player -> matchesCriteria(player, criteria))
                    .toList();

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), filtered.size());

            List<Player> pageContent = filtered.subList(start, end);

            return new PageImpl<>(pageContent, pageable, filtered.size());
        }

        private boolean matchesCriteria(Player player, PlayerSearchCriteria criteria) {
            if (criteria.getUsername() != null &&
                    !player.getUsername().toLowerCase().contains(criteria.getUsername().toLowerCase())) {
                return false;
            }

            if (criteria.getPreferredRole() != null &&
                    !player.getPreferredRole().equals(criteria.getPreferredRole())) {
                return false;
            }

            if (criteria.getRegion() != null &&
                    !player.getRegion().equals(criteria.getRegion())) {
                return false;
            }

            if (criteria.getMinLevel() != null &&
                    player.getSkillLevel() < criteria.getMinLevel()) {
                return false;
            }

            if (criteria.getMaxLevel() != null &&
                    player.getSkillLevel() > criteria.getMaxLevel()) {
                return false;
            }

            return true;
        }

        @Override
        public void deleteById(String id) {
            storage.remove(id);
        }
    }
}