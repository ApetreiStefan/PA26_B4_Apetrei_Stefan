package org.actors;

import org.model.Maze;
import java.util.*;

public class Robot extends Actor {
    public Robot(int actorId, int positionX, int positionY, Maze maze) { 
        super(actorId, positionX, positionY, maze); 
        this.movementDelayMs = 1500;
    }

    @Override
    public void run() {
        while (!maze.isGameOver() && isAlive) {
            applySpeedAndStop();
            if (maze.isGameOver() || !isAlive) break;

            maze.getSharedMemory().markVisited(positionX, positionY);

            int knownBunnyX = maze.getSharedMemory().getKnownX();
            int knownBunnyY = maze.getSharedMemory().getKnownY();

            if (knownBunnyX != -1) {
                moveToward(knownBunnyX, knownBunnyY);
            } else {
                exploreSystematically();
            }

            for (Bunny bunny : maze.getBunnies()) {
                if (!bunny.isAlive()) continue;
                if (isAdjacent(this.positionX, this.positionY, bunny.getPositionX(), bunny.getPositionY())) {
                    maze.bunnyCaught(bunny, this);
                }
            }

            for (Bunny bunny : maze.getBunnies()) {
                if (!bunny.isAlive()) continue;
                if (isAdjacent(this.positionX, this.positionY, bunny.getPositionX(), bunny.getPositionY())) {
                    maze.getSharedMemory().reportBunny(bunny.getPositionX(), bunny.getPositionY());
                }
            }
        }
    }

    private void exploreSystematically() {
        int[] deltaX = { 0, 0, 1, -1, -1, 1, -1, 1 };
        int[] deltaY = { 1, -1, 0, 0, -1, -1, 1, 1 };
        
        int minimumVisits = Integer.MAX_VALUE;
        List<int[]> candidates = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int nextX = positionX + deltaX[i];
            int nextY = positionY + deltaY[i];

            if (nextX >= 0 && nextX < maze.getSize() && nextY >= 0 && nextY < maze.getSize()) {
                if (!maze.getCell(nextX, nextY).isWall()) {
                    int visitCount = maze.getSharedMemory().getVisitCount(nextX, nextY);
                    if (visitCount < minimumVisits) {
                        minimumVisits = visitCount;
                        candidates.clear();
                        candidates.add(new int[]{nextX, nextY});
                    } else if (visitCount == minimumVisits) {
                        candidates.add(new int[]{nextX, nextY});
                    }
                }
            }
        }

        if (!candidates.isEmpty()) {
            int[] move = candidates.get(randomGenerator.nextInt(candidates.size()));
            if (maze.getCell(move[0], move[1]).enter()) {
                maze.getCell(positionX, positionY).leave();
                this.positionX = move[0];
                this.positionY = move[1];
            }
        }
    }

    private boolean isAdjacent(int robotX, int robotY, int bunnyX, int bunnyY) {
        return Math.abs(robotX - bunnyX) <= 1 && Math.abs(robotY - bunnyY) <= 1;
    }

    private void moveToward(int targetX, int targetY) {
        if (positionX == targetX && positionY == targetY) {
            exploreSystematically();
            return;
        }

        int gridSize = maze.getSize();
        int[][] distanceMatrix = new int[gridSize][gridSize];
        int[][] parentXMatrix = new int[gridSize][gridSize];
        int[][] parentYMatrix = new int[gridSize][gridSize];
        for(int i = 0; i < gridSize; i++) Arrays.fill(distanceMatrix[i], -1);

        Queue<int[]> searchQueue = new LinkedList<>();
        searchQueue.add(new int[]{positionX, positionY});
        distanceMatrix[positionX][positionY] = 0;

        int[] stepX = { 0, 0, 1, -1, -1, 1, -1, 1 };
        int[] stepY = { 1, -1, 0, 0, -1, -1, 1, 1 };

        boolean targetFound = false;
        while(!searchQueue.isEmpty() && !targetFound) {
            int[] currentPos = searchQueue.poll();
            int currentX = currentPos[0], currentY = currentPos[1];

            if (currentX == targetX && currentY == targetY) {
                targetFound = true;
                break;
            }

            for(int i = 0; i < 8; i++) {
                int neighborX = currentX + stepX[i], neighborY = currentY + stepY[i];
                if(neighborX >= 0 && neighborX < gridSize && neighborY >= 0 && neighborY < gridSize) {
                    if(!maze.getCell(neighborX, neighborY).isWall() && distanceMatrix[neighborX][neighborY] == -1) {
                        distanceMatrix[neighborX][neighborY] = distanceMatrix[currentX][currentY] + 1;
                        parentXMatrix[neighborX][neighborY] = currentX; 
                        parentYMatrix[neighborX][neighborY] = currentY;
                        searchQueue.add(new int[]{neighborX, neighborY});
                    }
                }
            }
        }

        if (targetFound) {
            int pathX = targetX, pathY = targetY;
            while(parentXMatrix[pathX][pathY] != positionX || parentYMatrix[pathX][pathY] != positionY) {
                int tempX = parentXMatrix[pathX][pathY];
                int tempY = parentYMatrix[pathX][pathY];
                pathX = tempX; pathY = tempY;
            }
            if (maze.getCell(pathX, pathY).enter()) {
                maze.getCell(positionX, positionY).leave();
                this.positionX = pathX; this.positionY = pathY;
            } else {
                exploreSystematically(); 
            }
        } else {
            exploreSystematically();
        }
    }
}