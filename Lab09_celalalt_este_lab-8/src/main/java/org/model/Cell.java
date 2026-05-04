package org.model;

public class Cell {
    private boolean isObstacle = false;
    private boolean isCurrentlyOccupied = false;

    public void setWall(boolean wall) { isObstacle = wall; }
    public boolean isWall() { return isObstacle; }

    public synchronized boolean enter() {
        if (isCurrentlyOccupied || isObstacle) return false;
        isCurrentlyOccupied = true;
        return true;
    }

    public synchronized void leave() {
        isCurrentlyOccupied = false;
    }
}