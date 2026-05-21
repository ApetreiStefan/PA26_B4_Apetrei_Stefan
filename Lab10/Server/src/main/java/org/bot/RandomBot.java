package org.bot;

import org.logic.Game;
import org.model.Question;

import java.util.Random;

public class RandomBot implements BotPlayer, Runnable {
    private final String name;
    private final Game game;
    private final Random random = new Random();

    public RandomBot(String name, Game game) {
        this.name = name;
        this.game = game;
        game.addPlayer(name, true);
    }

    @Override
    public void play(Game game) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (!game.isFinished()) {
            if (game.isStarted()) {
                Question q = game.getCurrentQuestion();
                if (q != null) {
                    try {
                        // Simulate thinking time
                        long thinkTime = 1000 + random.nextInt(4000);
                        Thread.sleep(thinkTime);
                        
                        int answer = random.nextInt(q.getOptions().size());
                        game.submitAnswer(name, answer, thinkTime);
                        System.out.println(name + " answered: " + answer);
                        
                        // Wait for next question (simplified polling)
                        while (q == game.getCurrentQuestion() && !game.isFinished()) {
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
