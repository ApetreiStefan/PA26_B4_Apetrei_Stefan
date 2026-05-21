package org;

import org.bot.CustomBot;
import org.bot.LLMBot;
import org.bot.RandomBot;
import org.benchmark.RepositoryBenchmark;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            String mode = args[0].toLowerCase();
            if ("cache".equals(mode)) {
                CachePerformanceTest.main(args);
                return;
            } else if ("benchmark".equals(mode)) {
                try {
                    RepositoryBenchmark.main(args);
                } catch (Exception e) {
                    System.err.println("Error running JMH Benchmark: " + e.getMessage());
                    e.printStackTrace();
                }
                return;
            }
        }

        // Default mode: Run GameServer
        System.out.println("Starting GameServer in multiplayer mode...");
        GameServer server = new GameServer();
        
        // Add some bots for demonstration (Advanced Requirement)
        new RandomBot("Bot-Random", server.getGame()).play(server.getGame());
        new CustomBot("Bot-Genius", server.getGame()).play(server.getGame());
        new LLMBot("Bot-GPT4", server.getGame(), "Hard").play(server.getGame());

        try {
            server.run();
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}
