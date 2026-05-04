package org.model;

public class SharedMemory {
    private int knownBunnyPositionX = -1;
    private int knownBunnyPositionY = -1;
    private final int[][] cellVisitCounts;

    public SharedMemory(int gridSize) {
        this.cellVisitCounts = new int[gridSize][gridSize];
    }

    public synchronized void reportBunny(int positionX, int positionY) {
        this.knownBunnyPositionX = positionX;
        this.knownBunnyPositionY = positionY;
    }

    public synchronized void markVisited(int positionX, int positionY) {
        if (positionX >= 0 && positionX < cellVisitCounts.length && positionY >= 0 && positionY < cellVisitCounts.length) {
            cellVisitCounts[positionX][positionY]++;
        }
    }

    public synchronized int getVisitCount(int positionX, int positionY) {
        if (positionX >= 0 && positionX < cellVisitCounts.length && positionY >= 0 && positionY < cellVisitCounts.length) {
            return cellVisitCounts[positionX][positionY];
        }
        return Integer.MAX_VALUE;
    }

    public synchronized String getInfo() {
        if (knownBunnyPositionX == -1) return "Searching...";
        return String.format("(%d, %d)", knownBunnyPositionX, knownBunnyPositionY);
    }

    public synchronized int getKnownX() { return knownBunnyPositionX; }
    public synchronized int getKnownY() { return knownBunnyPositionY; }
}