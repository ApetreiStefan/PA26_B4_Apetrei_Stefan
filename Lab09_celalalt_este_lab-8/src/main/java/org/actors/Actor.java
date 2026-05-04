package org.actors;

import java.util.Random;
import org.model.Maze;

public abstract class Actor implements Runnable {
    protected int actorId;
    protected int positionX, positionY;
    protected final Maze maze;
    protected final Random randomGenerator = new Random();

    protected volatile int movementDelayMs = 300;
    protected volatile boolean isPaused = false;
    protected volatile boolean isAlive = true;

    public Actor(int actorId, int positionX, int positionY, Maze maze) {
        this.actorId = actorId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.maze = maze;
    }

    protected void applySpeedAndStop() {
        try {
            while (isPaused && !maze.isGameOver() && isAlive) {
                Thread.sleep(100);
            }
            Thread.sleep(movementDelayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public int getActorId() { return actorId; }
    public void setMovementDelayMs(int delayMs) { this.movementDelayMs = Math.max(10, delayMs); }
    public void setIsPaused(boolean paused) { this.isPaused = paused; }
    public boolean isAlive() { return isAlive; }
    public void setAlive(boolean alive) { this.isAlive = alive; }
}