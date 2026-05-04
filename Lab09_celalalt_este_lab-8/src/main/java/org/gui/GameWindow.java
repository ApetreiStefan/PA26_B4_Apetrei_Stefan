package org.gui;

import org.model.Maze;
import org.actors.Bunny;
import org.actors.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private final Maze maze;
    private final SimulationPanel simulationPanel;

    public GameWindow(Maze maze) {
        this.maze = maze;
        this.setTitle("Robot Chase Simulation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);

        this.simulationPanel = new SimulationPanel(maze);
        this.add(simulationPanel);

        Timer refreshTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulationPanel.repaint();
                if (maze.isGameOver()) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        refreshTimer.start();
    }

    private static class SimulationPanel extends JPanel {
        private final Maze maze;

        public SimulationPanel(Maze maze) {
            this.maze = maze;
            this.setBackground(Color.DARK_GRAY);
        }

        @Override
        protected void paintComponent(Graphics graphicsContext) {
            super.paintComponent(graphicsContext);
            int gridSize = maze.getSize();
            int cellWidth = getWidth() / gridSize;
            int cellHeight = getHeight() / gridSize;

            for (int x = 0; x < gridSize; x++) {
                for (int y = 0; y < gridSize; y++) {
                    int pixelX = x * cellWidth;
                    int pixelY = y * cellHeight;

                    if (maze.getCell(x, y).isWall()) {
                        graphicsContext.setColor(Color.GRAY);
                        graphicsContext.fillRect(pixelX, pixelY, cellWidth, cellHeight);
                    } else if (x == maze.getExitX() && y == maze.getExitY()) {
                        graphicsContext.setColor(Color.GREEN);
                        graphicsContext.fillRect(pixelX, pixelY, cellWidth, cellHeight);
                        graphicsContext.setColor(Color.BLACK);
                        graphicsContext.drawString("EXIT", pixelX + cellWidth/4, pixelY + cellHeight/2);
                    } else {
                        graphicsContext.setColor(Color.BLACK);
                        graphicsContext.fillRect(pixelX, pixelY, cellWidth, cellHeight);
                        graphicsContext.setColor(Color.DARK_GRAY);
                        graphicsContext.drawRect(pixelX, pixelY, cellWidth, cellHeight);
                    }

                    for (Bunny bunny : maze.getBunnies()) {
                        if (bunny.isAlive() && bunny.getPositionX() == x && bunny.getPositionY() == y) {
                            graphicsContext.setColor(Color.ORANGE);
                            graphicsContext.fillOval(pixelX + 2, pixelY + 2, cellWidth - 4, cellHeight - 4);
                            graphicsContext.setColor(Color.BLACK);
                            graphicsContext.drawString("B" + bunny.getActorId(), pixelX + cellWidth/3, pixelY + cellHeight/2 + 4);
                        }
                    }

                    for (Robot robot : maze.getRobots()) {
                        if (robot.getPositionX() == x && robot.getPositionY() == y) {
                            graphicsContext.setColor(Color.BLUE);
                            graphicsContext.fillOval(pixelX + 2, pixelY + 2, cellWidth - 4, cellHeight - 4);
                            graphicsContext.setColor(Color.WHITE);
                            graphicsContext.drawString("R" + robot.getActorId(), pixelX + cellWidth/3, pixelY + cellHeight/2 + 4);
                        }
                    }
                }
            }
        }
    }
}
