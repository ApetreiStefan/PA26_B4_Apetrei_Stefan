package org.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Player {
    private String name;
    private int score;
    private long totalResponseTime; // in milliseconds
    private boolean isBot;

    public Player(String name, boolean isBot) {
        this.name = name;
        this.score = 0;
        this.totalResponseTime = 0;
        this.isBot = isBot;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void addResponseTime(long time) {
        this.totalResponseTime += time;
    }
}
