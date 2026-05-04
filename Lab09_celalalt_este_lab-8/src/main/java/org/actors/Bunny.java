package org.actors;

import org.model.Maze;

public class Bunny extends Actor {
    public Bunny(int actorId, int positionX, int positionY, Maze maze) {
        super(actorId, positionX, positionY, maze);
        this.movementDelayMs = 1000;
    }

    private void moveRandomly() {
        int[] deltaX = { 0, 0, 1, -1 };
        int[] deltaY = { 1, -1, 0, 0 };

        int direction = randomGenerator.nextInt(4);
        int nextX = Math.max(0, Math.min(maze.getSize() - 1, positionX + deltaX[direction]));
        int nextY = Math.max(0, Math.min(maze.getSize() - 1, positionY + deltaY[direction]));

        if (maze.getCell(nextX, nextY).enter()) {
            maze.getCell(positionX, positionY).leave();
            this.positionX = nextX;
            this.positionY = nextY;
        }
    }

    @Override
    public void run() {
        while (!maze.isGameOver() && isAlive) {
            applySpeedAndStop();
            if (maze.isGameOver() || !isAlive)
                break;

            int distanceToExit = Math.abs(positionX - maze.getExitX()) + Math.abs(positionY - maze.getExitY());

            if (distanceToExit <= 5) {
                moveToward(maze.getExitX(), maze.getExitY());
            } else {
                moveRandomly();
            }

            for (Robot robot : maze.getRobots()) {
                if (Math.abs(this.positionX - robot.getPositionX()) <= 1
                        && Math.abs(this.positionY - robot.getPositionY()) <= 1) {
                    maze.bunnyCaught(this, robot);
                    return;
                }
            }

            if (positionX == maze.getExitX() && positionY == maze.getExitY()) {
                maze.bunnyEscaped(this);
                return;
            }
        }
    }

    private void moveToward(int targetX, int targetY) {
        if (positionX == targetX && positionY == targetY)
            return;

        int gridSize = maze.getSize();
        int[][] distanceMatrix = new int[gridSize][gridSize];
        int[][] parentXMatrix = new int[gridSize][gridSize];
        int[][] parentYMatrix = new int[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++)
            java.util.Arrays.fill(distanceMatrix[i], -1);

        java.util.Queue<int[]> searchQueue = new java.util.LinkedList<>();
        searchQueue.add(new int[] { positionX, positionY });
        distanceMatrix[positionX][positionY] = 0;

        int[] stepX = { 0, 0, 1, -1, -1, 1, -1, 1 };
        int[] stepY = { 1, -1, 0, 0, -1, -1, 1, 1 };

        boolean targetFound = false;
        while (!searchQueue.isEmpty() && !targetFound) {
            int[] currentPos = searchQueue.poll();
            int currentX = currentPos[0], currentY = currentPos[1];

            if (currentX == targetX && currentY == targetY) {
                targetFound = true;
                break;
            }

            for (int i = 0; i < 8; i++) {
                int neighborX = currentX + stepX[i], neighborY = currentY + stepY[i];
                if (neighborX >= 0 && neighborX < gridSize && neighborY >= 0 && neighborY < gridSize) {
                    if (!maze.getCell(neighborX, neighborY).isWall() && distanceMatrix[neighborX][neighborY] == -1) {
                        distanceMatrix[neighborX][neighborY] = distanceMatrix[currentX][currentY] + 1;
                        parentXMatrix[neighborX][neighborY] = currentX;
                        parentYMatrix[neighborX][neighborY] = currentY;
                        searchQueue.add(new int[] { neighborX, neighborY });
                    }
                }
            }
        }

        if (targetFound) {
            int pathX = targetX, pathY = targetY;
            while (parentXMatrix[pathX][pathY] != positionX || parentYMatrix[pathX][pathY] != positionY) {
                int tempX = parentXMatrix[pathX][pathY];
                int tempY = parentYMatrix[pathX][pathY];
                pathX = tempX;
                pathY = tempY;
            }
            if (maze.getCell(pathX, pathY).enter()) {
                maze.getCell(positionX, positionY).leave();
                this.positionX = pathX;
                this.positionY = pathY;
            }
        }
    }
}