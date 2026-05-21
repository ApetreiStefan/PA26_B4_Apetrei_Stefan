package org.bot;

import org.logic.Game;
import org.model.Question;

public class CustomBot implements BotPlayer, Runnable {
    private final String name;
    private final Game game;

    public CustomBot(String name, Game game) {
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
                        // Custom bots are fast!
                        long thinkTime = 500;
                        Thread.sleep(thinkTime);
                        
                        // It "knows" the answer
                        int answer = q.getCorrectIndex();
                        game.submitAnswer(name, answer, thinkTime);
                        System.out.println(name + " (CustomBot) answered correctly!");
                        
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
