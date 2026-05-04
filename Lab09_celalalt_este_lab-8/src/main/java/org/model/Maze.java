package org.model;

import org.actors.Actor;
import org.actors.Bunny;
import org.actors.Robot;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Maze {
    private final int gridSize;
    private final Cell[][] mazeGrid;
    private final SharedMemory sharedMemory;
    private final AtomicBoolean isSimulationOver = new AtomicBoolean(false);
    private final int exitPositionX, exitPositionY;

    private final List<Bunny> activeBunnies = new CopyOnWriteArrayList<>();
    private final List<Robot> activeRobots = new CopyOnWriteArrayList<>();
    private long simulationStartTime;
    private final long executionTimeLimitMs = 60000;

    private static final String COLOR_RESET  = "\u001B[0m";
    private static final String COLOR_RED    = "\u001B[31m";
    private static final String COLOR_GREEN  = "\u001B[32m";
    private static final String COLOR_BLUE   = "\u001B[34m";
    private static final String COLOR_YELLOW = "\u001B[33m";
    private static final String COLOR_PURPLE = "\u001B[35m";
    private static final String COLOR_GRAY   = "\u001B[90m";

    public Maze(int gridSize) {
        this.gridSize = gridSize;
        this.mazeGrid = new Cell[gridSize][gridSize];
        this.exitPositionX = gridSize - 1;
        this.exitPositionY = gridSize - 1;
        this.sharedMemory = new SharedMemory(gridSize);
        initializeMazeStructure();
    }

    private void initializeMazeStructure() {
        Random random = new Random();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                mazeGrid[i][j] = new Cell();
                if (random.nextDouble() < 0.1 && !(i == exitPositionX && j == exitPositionY)) {
                    mazeGrid[i][j].setWall(true);
                }
            }
        }
    }

    public void startSimulation(int numberOfRobots, int numberOfBunnies) {
        Random random = new Random();
        
        for (int i = 0; i < numberOfBunnies; i++) {
            int startX, startY;
            do {
                startX = random.nextInt(gridSize - 1);
                startY = random.nextInt(gridSize - 1);
            } while (mazeGrid[startX][startY].isWall());
            mazeGrid[startX][startY].enter();
            activeBunnies.add(new Bunny(i + 1, startX, startY, this));
        }

        for (int i = 0; i < numberOfRobots; i++) {
            int startX, startY;
            do {
                startX = random.nextInt(gridSize);
                startY = random.nextInt(gridSize);
            } while (mazeGrid[startX][startY].isWall() || !mazeGrid[startX][startY].enter());
            activeRobots.add(new Robot(i + 1, startX, startY, this));
        }

        simulationStartTime = System.currentTimeMillis();

        for (Bunny bunny : activeBunnies) {
            new Thread(bunny, "Bunny-" + bunny.getActorId()).start();
        }
        for (Robot robot : activeRobots) {
            new Thread(robot, "Robot-" + robot.getActorId()).start();
        }

        startDaemonManager();
        startCommandListener();
    }

    private void startDaemonManager() {
        Thread manager = new Thread(() -> {
            while (!isSimulationOver.get()) {
                long elapsed = System.currentTimeMillis() - simulationStartTime;
                if (elapsed > executionTimeLimitMs) {
                    setGameOver(COLOR_RED + "Time limit exceeded! Game over." + COLOR_RESET);
                    break;
                }
                renderGameState(elapsed);
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
            }
        }, "DaemonManager");
        manager.setDaemon(true);
        manager.start();
    }

    private void startCommandListener() {
        Thread listener = new Thread(() -> {
            Scanner inputScanner = new Scanner(System.in);
            while (!isSimulationOver.get() && inputScanner.hasNextLine()) {
                String input = inputScanner.nextLine().trim();
                if (input.isEmpty()) continue;
                executeCommand(input);
            }
        }, "CommandListener");
        listener.setDaemon(true);
        listener.start();
    }

    private void executeCommand(String commandLine) {
        String[] segments = commandLine.toLowerCase().split("\\s+");
        if (segments.length < 3) return;

        String action = segments[0];
        String actorType = segments[1];
        String targetId = segments[2];

        List<? extends Actor> targetList = actorType.equals("bunny") ? activeBunnies : (actorType.equals("robot") ? activeRobots : null);
        if (targetList == null) return;

        for (Actor actor : targetList) {
            if (targetId.equals("all") || targetId.equals(String.valueOf(actor.getActorId()))) {
                if (action.equals("stop")) {
                    actor.setIsPaused(true);
                } else if (action.equals("resume")) {
                    actor.setIsPaused(false);
                } else if (action.equals("speed") && segments.length == 4) {
                    try {
                        int delay = Integer.parseInt(segments[3]);
                        actor.setMovementDelayMs(delay);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
    }

    private synchronized void renderGameState(long elapsedMs) {
        StringBuilder displayBuffer = new StringBuilder("\033[H\033[2J");
        displayBuffer.append(COLOR_PURPLE).append("=== ROBOT CHASE SIMULATION ===").append(COLOR_RESET).append("\n");
        displayBuffer.append(COLOR_GRAY).append("Time: ").append(elapsedMs / 1000).append("s / ").append(executionTimeLimitMs / 1000).append("s\n\n").append(COLOR_RESET);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                String cellIcon = "  ";

                if (mazeGrid[i][j].isWall()) {
                    cellIcon = COLOR_GRAY + "██" + COLOR_RESET;
                } else if (i == exitPositionX && j == exitPositionY) {
                    cellIcon = COLOR_GREEN + "🏁" + COLOR_RESET;
                }

                int bunnyPresence = 0;
                for (Bunny bunny : activeBunnies) {
                    if (bunny.isAlive() && bunny.getPositionX() == i && bunny.getPositionY() == j) {
                        bunnyPresence++;
                    }
                }
                if (bunnyPresence > 0) {
                    cellIcon = COLOR_YELLOW + "🐰" + COLOR_RESET;
                }

                for (Robot robot : activeRobots) {
                    if (robot.getPositionX() == i && robot.getPositionY() == j) {
                        cellIcon = COLOR_BLUE + "🤖" + COLOR_RESET;
                    }
                }
                displayBuffer.append("[").append(cellIcon).append("]");
            }
            displayBuffer.append("\n");
        }

        displayBuffer.append("\nShared Memory (Bunny Last Seen): ").append(COLOR_YELLOW).append(sharedMemory.getInfo()).append(COLOR_RESET);
        displayBuffer.append("\nStatus: ").append(isSimulationOver.get() ? COLOR_RED + "GAME OVER" : COLOR_GREEN + "ACTIVE").append(COLOR_RESET);
        displayBuffer.append("\nCommands: speed/stop/resume <robot/bunny> <id/all> [ms]\n> ");

        System.out.print(displayBuffer.toString());
        System.out.flush();
    }

    public synchronized void bunnyCaught(Bunny bunny, Robot robot) {
        if (!bunny.isAlive()) return;
        bunny.setAlive(false);
        System.out.println(COLOR_RED + "\nCATCH! Robot " + robot.getActorId() + " caught Bunny " + bunny.getActorId() + " at (" + bunny.getPositionX() + "," + bunny.getPositionY() + ")! 🤖💥🐰" + COLOR_RESET);
        mazeGrid[bunny.getPositionX()][bunny.getPositionY()].leave();
        evaluateTermination();
    }

    public synchronized void bunnyEscaped(Bunny bunny) {
        if (!bunny.isAlive()) return;
        bunny.setAlive(false);
        System.out.println(COLOR_GREEN + "\nVICTORY! Bunny " + bunny.getActorId() + " reached the exit! 🐰🏁" + COLOR_RESET);
        mazeGrid[bunny.getPositionX()][bunny.getPositionY()].leave();
        evaluateTermination();
    }

    private void evaluateTermination() {
        boolean anyBunnyAlive = false;
        for (Bunny bunny : activeBunnies) {
            if (bunny.isAlive()) {
                anyBunnyAlive = true;
                break;
            }
        }
        if (!anyBunnyAlive) {
            setGameOver("All bunnies have reached their outcome.");
        }
    }

    public void setGameOver(String message) {
        if (isSimulationOver.compareAndSet(false, true)) {
            System.out.println("\n" + message);
            for(Bunny bunny : activeBunnies) bunny.setAlive(false);
            for(Robot robot : activeRobots) robot.setAlive(false);
        }
    }

    public int getSize() { return gridSize; }
    public Cell getCell(int x, int y) { return mazeGrid[x][y]; }
    public boolean isGameOver() { return isSimulationOver.get(); }
    public SharedMemory getSharedMemory() { return sharedMemory; }
    public List<Bunny> getBunnies() { return activeBunnies; }
    public List<Robot> getRobots() { return activeRobots; }
    public int getExitX() { return exitPositionX; }
    public int getExitY() { return exitPositionY; }
}