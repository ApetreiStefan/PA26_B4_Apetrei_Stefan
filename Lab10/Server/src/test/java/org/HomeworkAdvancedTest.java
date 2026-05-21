package org;

import org.entity.GameEntity;
import org.entity.PlayerEntity;
import org.entity.ResultEntity;
import org.repository.GameRepository;
import org.repository.PlayerRepository;
import org.repository.ResultRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

public class HomeworkAdvancedTest {

    @Test
    public void testHomeworkAndAdvancedSpecs() {
        PlayerRepository playerRepo = new PlayerRepository();
        GameRepository gameRepo = new GameRepository();
        ResultRepository resultRepo = new ResultRepository();

        System.out.println("\n--- RUNNING HOMEWORK & ADVANCED JPA TESTS ---\n");

        // 1. Test JPQL Modifying Transactional Query
        System.out.println("Testing transactional JPQL modifying query...");
        String uniquePrefix = "BotPlayer_" + System.currentTimeMillis();
        playerRepo.create(new PlayerEntity(uniquePrefix + "_1", false));
        playerRepo.create(new PlayerEntity(uniquePrefix + "_2", false));
        
        int updated = playerRepo.updateBotStatusByNamePattern(uniquePrefix + "%", true);
        assertEquals(2, updated);

        // 2. Test JPQL Read Query
        System.out.println("Testing JPQL read query...");
        List<PlayerEntity> bots = playerRepo.findBots();
        assertFalse(bots.isEmpty());
        assertTrue(bots.stream().anyMatch(b -> b.getName().equals(uniquePrefix + "_1") && b.isBot()));

        // 3. Test One-To-Many & Many-To-Many relationship structure
        System.out.println("Testing relationships & cascading...");
        PlayerEntity player = playerRepo.findByName(uniquePrefix + "_1");
        assertNotNull(player);

        GameEntity game = new GameEntity();
        ResultEntity result = new ResultEntity(player, game, 85, 3000);
        game.addResult(result);
        gameRepo.create(game);

        GameEntity savedGame = gameRepo.findById(game.getId());
        assertNotNull(savedGame);
        assertEquals(1, savedGame.getResults().size());
        assertEquals(player.getId(), savedGame.getResults().get(0).getPlayer().getId());

        // 4. Test Criteria API Dynamic Search with Filters (Advanced)
        System.out.println("Testing Criteria API Dynamic Search...");
        List<ResultEntity> filteredResults = resultRepo.findResultsWithFilters(
                uniquePrefix, // player prefix
                80,          // min score
                true,        // is bot
                LocalDateTime.now().minusHours(1) // game start time after
        );
        assertFalse(filteredResults.isEmpty());
        assertEquals(85, filteredResults.get(0).getScore());
    }
}
