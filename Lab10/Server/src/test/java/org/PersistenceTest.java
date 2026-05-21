package org;

import org.entity.PlayerEntity;
import org.entity.QuestionEntity;
import org.entity.GameEntity;
import org.entity.ResultEntity;
import org.repository.PlayerRepository;
import org.repository.QuestionRepository;
import org.repository.GameRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class PersistenceTest {

    @Test
    public void testPersistence() {
        PlayerRepository playerRepo = new PlayerRepository();
        QuestionRepository questionRepo = new QuestionRepository();
        GameRepository gameRepo = new GameRepository();

        // Clean up previous runs if they exist
        PlayerEntity existing = playerRepo.findByName("TestPlayer");
        // For simplicity in this test, we just use a different name or assume fresh DB
        String playerName = "TestPlayer_" + System.currentTimeMillis();

        // 1. Create and persist a player
        PlayerEntity player = new PlayerEntity(playerName, false);
        playerRepo.create(player);
        
        PlayerEntity savedPlayer = playerRepo.findByName(playerName);
        assertNotNull(savedPlayer);
        assertEquals(playerName, savedPlayer.getName());

        // 2. Create and persist a question
        QuestionEntity question = new QuestionEntity(
            "What is JPA?", 
            Arrays.asList("API", "Implementation", "Library", "Database"), 
            0
        );
        questionRepo.create(question);
        
        List<QuestionEntity> questions = questionRepo.findAll();
        assertFalse(questions.isEmpty());
        assertEquals("What is JPA?", questions.get(0).getText());

        // 3. Create a game and result
        GameEntity game = new GameEntity();
        ResultEntity result = new ResultEntity(player, game, 100, 5000);
        game.addResult(result);
        
        gameRepo.create(game);

        GameEntity savedGame = gameRepo.findById(game.getId());
        assertNotNull(savedGame);
        assertEquals(1, savedGame.getResults().size());
        assertEquals(100, savedGame.getResults().get(0).getScore());
    }
}
